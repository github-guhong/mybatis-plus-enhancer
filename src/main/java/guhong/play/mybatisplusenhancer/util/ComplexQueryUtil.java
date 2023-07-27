package guhong.play.mybatisplusenhancer.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import guhong.play.mybatisplusenhancer.annotation.relation.As;
import guhong.play.mybatisplusenhancer.annotation.relation.Column;
import guhong.play.mybatisplusenhancer.annotation.relation.Join;
import guhong.play.mybatisplusenhancer.annotation.relation.SubQuery;
import guhong.play.mybatisplusenhancer.constants.enums.JoinType;
import guhong.play.mybatisplusenhancer.exception.ComplexQueryException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 复杂查询工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Slf4j
@SuppressWarnings("all")
public class ComplexQueryUtil {

    /**
     * 结果类型j
     */
    private Class<?> resultClass;

    /**
     * 原始实体类型
     */
    private Class<?> entityClass;


    private IService service;
    /**
     * 主表
     */
    private String primaryTable;

    public ComplexQueryUtil(Class<?> resultClass, IService service, Class<?> entityClass) {
        this.resultClass = resultClass;
        this.entityClass = entityClass;
        this.primaryTable = CrudUtil.getTableNameByEntityClass(entityClass);
        this.service = service;
    }

    /**
     * 是否需要复杂查询
     * resultClass不等于Object且resultClass和entityClass不相等就说明制定了结果类型，就需要复杂查询
     */
    public boolean isNeedComplexQuery() {
        return !Object.class.equals(resultClass) && !resultClass.equals(entityClass);
    }

    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param queryWrapper mybatis plus 条件对象
     * @return 返回分页数据
     */
    public IPage page(IPage page, AbstractWrapper queryWrapper) {
        String sql = this.getFinalSql(queryWrapper, page);
        String countSql = this.toCountSql(sql);
        Long count = queryCount(countSql);
        if (count == null || 0 == count) {
            return page;
        }
        page.setTotal(count);
        return queryPage(sql, page);
    }

    /**
     * 查询单个对象
     *
     * @param queryWrapper mybatis plus 条件对象
     * @return 返回单个对象
     */
    public Object getSingle(AbstractWrapper queryWrapper) {
        String sql = this.getFinalSql(queryWrapper, null);
        return querySingle(sql);
    }

    /**
     * 返回列表数据
     *
     * @param queryWrapper mybatis plus 条件对象
     * @return 返回列表数据
     */
    public List list(AbstractWrapper queryWrapper) {
        String sql = this.getFinalSql(queryWrapper, null);
        return queryList(sql);
    }

    /**
     * 获得最终的sql
     *
     * @param queryWrapper 条件构造器
     * @param page         分页对象
     * @return 返回最终的sql
     */
    private String getFinalSql(AbstractWrapper queryWrapper, IPage page) {
        String tableName = CrudUtil.getTableNameByService(service);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT").append(" \n");
        sql.append(this.getSelectColumns()).append(" \n");
        sql.append("FROM " + tableName).append(" \n");
        sql.append(this.getJoinSql()).append(" \n");

        // 检测逻辑删除
        Field field = ToolKit.getAnnotationField(entityClass, TableLogic.class);
        if (field != null) {
            String formattedField = CrudUtil.formatField(field.getName());
            queryWrapper.ne(tableName + "." + formattedField, 1);
        }
        sql.append(queryWrapper.getCustomSqlSegment()).append(" \n");

        // 拼接limit
        if (page != null) {
            long pageNo = (page.getCurrent() - 1) * page.getSize();
            long limit = page.getSize();
            sql.append("limit " + pageNo + " , " + limit);
        }
        String finalSql = sql.toString();
        Map<String, Object> params = queryWrapper.getParamNameValuePairs();
        for (String key : params.keySet()) {
            String value = null;
            Object o = params.get(key);
            if (o instanceof String) {
                value = "'" + o + "'";
            } else {
                value = o.toString();
            }
            String oldParam = "#{ew.paramNameValuePairs." + key + "}";
            finalSql = finalSql.replace(oldParam, value);
        }
        return finalSql;
    }

    /**
     * 将sql转为count的形式
     *
     * @param sourceSql 源sql
     * @return 返回查询count的sql语句
     */
    private String toCountSql(String sourceSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(1) FROM ( ").append(" \n");
        sql.append(cleanOrderBy(sourceSql)).append("\n");
        sql.append(" ) total");
        return sql.toString();
    }

    private String cleanOrderBy(String sourceSql) {
        try {
            Select selectSql = (Select) CCJSqlParserUtil.parse(sourceSql);
            PlainSelect plainSelect = (PlainSelect) selectSql.getSelectBody();
            plainSelect.setOrderByElements(null);
            return selectSql.toString();
        } catch (JSQLParserException e) {
            log.error("SQL解析出现异常【"+sourceSql+"】。异常信息：" + ToolKit.getSimpExcpetionMessage(e));
        }
        return sourceSql;
    }

    /**
     * 获得查询列名
     *
     * @return 返回要查询的列名
     */
    private String getSelectColumns() {
        Set<String> selectColumnList = this.getSelectColumns(resultClass, primaryTable);
        return CollectionUtil.join(selectColumnList, ",");
    }

    /**
     * 递归获取查询的列
     *
     * @param queryClass     查询的类
     * @param queryTableName 查询的表名
     * @return 返回
     */
    private Set<String> getSelectColumns(Class<?> queryClass, String queryTableName) {
        Set<String> selectColumnList = CollectionUtil.newHashSet();
        Field[] fields = ReflectUtil.getFields(queryClass);
        for (Field field : fields) {
            String tableName = queryTableName;
            String fieldName = field.getName();
            String formattedField = CrudUtil.formatField(fieldName);
            String columnName = tableName + "." + formattedField;

            // 同一个字段子查询和连表操作只能生效一个
            SubQuery subQuery = field.getAnnotation(SubQuery.class);
            if (subQuery != null) {
                String subQuerySql = subQuery.sql();
                columnName = "(" + subQuerySql + ")";
            } else {
                Join join = field.getAnnotation(Join.class);
                if (join != null) {
                    Class<?> fieldType = field.getType();
                    tableName = join.foreignTable();
                    if (Collection.class.isAssignableFrom(fieldType)) {
                        // 集合类型

                        // 获得集合类型的泛型
                        Type type = TypeUtil.getTypeArgument(TypeUtil.getType(field));
                        Class genericType = ClassUtil.loadClass(type.getTypeName());
                        if (ToolKit.isCustomtType(genericType)) {
                            columnName = null;
                            selectColumnList.addAll(this.getSelectColumns(genericType, tableName));
                        }
                    } else if (ToolKit.isCustomtType(fieldType)) {
                        // 自定义对象类型
                        columnName = null;
                        selectColumnList.addAll(this.getSelectColumns(fieldType, tableName));
                    } else {
                        columnName = tableName + "." + formattedField;
                    }
                }
            }
            if (columnName != null) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    columnName = tableName + "." + CrudUtil.formatField(column.name()) + " as " + fieldName;
                }
                As as = field.getAnnotation(As.class);
                if (as != null && column == null) {
                    String asValue = as.name();
                    if (StrUtil.isBlank(asValue)) {
                        asValue = fieldName;
                    }
                    columnName += " as " + asValue;
                }
                selectColumnList.add(columnName);
            }

        }
        return selectColumnList;
    }

    /**
     * 根据结果类型得到join sql
     *
     * @return 返回 join sql
     */
    private String getJoinSql() {
        Set<String> joinSqlList = this.getJoinSql(resultClass);
        return CollectionUtil.join(joinSqlList, "\n");
    }

    /**
     * 递归获取查询的表连接
     *
     * @param joinClass 连接类
     * @return 返回查询的表连接sql列表
     */
    private Set<String> getJoinSql(Class<?> joinClass) {
        Set<String> joinSqlList = CollectionUtil.newHashSet();
        Field[] fields = ReflectUtil.getFields(joinClass);
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            Join join = field.getAnnotation(Join.class);
            if (join == null) {
                continue;
            }
            if (!ToolKit.isBaseType(fieldType)) {
                joinSqlList.addAll(getJoinSql(fieldType));
            }
            String foreignKey = join.foreignKey();
            String foreignTable = join.foreignTable();
            JoinType joinType = join.jonType();
            String primaryKey = join.primaryKey();
            String primaryTable = join.primaryTable();
            if (StrUtil.isBlank(primaryTable)) {
                primaryTable = this.primaryTable;
            }
            String joinSql = joinType.getValue() + " join " + foreignTable + " on " + primaryTable + "." + primaryKey + "=" + foreignTable + "." + foreignKey;
            joinSqlList.add(joinSql);
        }
        return joinSqlList;
    }

    /**
     * 查询Count
     *
     * @param sql sql片段
     * @return 返回查询结果集
     */
    private Long queryCount(String sql) {
        DataSource dataSource = SpringUtil.getBean(DataSource.class);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            printSql(sql);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetUtil resultSetUtil = new ResultSetUtil(resultSet);
            return resultSetUtil.toCount();
        } catch (Exception e) {
            throw new ComplexQueryException("执行复杂查询失败！" + e.getMessage());
        } finally {
            closeAll(resultSet, statement, connection);
        }
    }

    /**
     * 查询Count
     *
     * @param sql  sql片段
     * @param page 分页对象
     * @return 返回查询结果集
     */
    private IPage queryPage(String sql, IPage page) {
        DataSource dataSource = SpringUtil.getBean(DataSource.class);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            printSql(sql);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetUtil resultSetUtil = new ResultSetUtil(resultSet);
            return resultSetUtil.toPage(resultClass, page);
        } catch (Exception e) {
            throw new ComplexQueryException("执行复杂查询失败！" + e.getMessage());
        } finally {
            closeAll(resultSet, statement, connection);
        }
    }

    /**
     * 查询单个对象
     *
     * @param sql sql片段
     * @return 返回查询结果集
     */
    private Object querySingle(String sql) {
        DataSource dataSource = SpringUtil.getBean(DataSource.class);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            printSql(sql);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetUtil resultSetUtil = new ResultSetUtil(resultSet);
            return resultSetUtil.toSingle(resultClass);
        } catch (Exception e) {
            throw new ComplexQueryException("执行复杂查询失败！" + e.getMessage());
        } finally {
            closeAll(resultSet, statement, connection);
        }
    }

    /**
     * 查询列表
     *
     * @param sql sql片段
     * @return 返回查询结果集
     */
    private List queryList(String sql) {
        DataSource dataSource = SpringUtil.getBean(DataSource.class);
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            printSql(sql);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetUtil resultSetUtil = new ResultSetUtil(resultSet);
            return resultSetUtil.toList(resultClass);
        } catch (Exception e) {
            throw new ComplexQueryException("执行复杂查询失败！" + e.getMessage());
        } finally {
            closeAll(resultSet, statement, connection);
        }
    }

    private void printSql(String finalSql) {
        System.out.println("=============================自定义查询SQL-头=============================");
        System.out.println("\n" + finalSql);
        System.out.println("=============================自定义查询SQL-尾=============================");
    }

    /**
     * 关闭全部接口
     *
     * @param resultSet 结果集对象
     * @param statement Statement对象
     * @param conn      Connection连接对象
     */
    private void closeAll(ResultSet resultSet, Statement statement, Connection conn) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(conn);
    }

    /**
     * 关闭Connection对象
     *
     * @param connection 指定的Connection对象
     */
    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                if (!connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new ComplexQueryException("关闭Connection对象异常，原因可能是" + e.getMessage());
        }
    }

    /**
     * 关闭Statement对象
     *
     * @param statement 指定的Statement对象
     */
    private void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                if (!statement.isClosed()) {
                    statement.close();
                }

            }
        } catch (SQLException e) {
            throw new ComplexQueryException("关闭" + statement.getClass().getSimpleName() + "对象异常，原因可能是" + e.getMessage());
        }
    }

    /**
     * 关闭ResultSet对象
     *
     * @param resultSet 指定的ResultSet对象
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }
        } catch (SQLException e) {
            throw new ComplexQueryException("关闭ResultSet对象异常，原因可能是" + e.getMessage());
        }
    }
}
