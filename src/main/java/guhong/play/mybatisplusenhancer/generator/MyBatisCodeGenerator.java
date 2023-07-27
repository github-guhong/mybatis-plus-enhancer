package guhong.play.mybatisplusenhancer.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis 代码生成器
 * @author 李双凯
 * @date 2019/9/4 20:22
 */
public class MyBatisCodeGenerator {

    public static void main(String[] args) throws Exception {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");

        start("author", dsc);
    }


    /**
     * 根据自己项目修改配置
     */
    public static void start(String author, DataSourceConfig dsc) throws Exception {
        if (StrUtil.isBlank(author)) {
            author = System.getProperty("user.name");
        }
        if (null == dsc) {
            System.out.println("你没有写数据源配置哦！");
        }

        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("输入表名，多个用逗号隔开（如：t_system_dictionary,t_user_info）：");
        String tables = scanner.readLine();
        while (StrUtil.isBlank(tables)) {
            tables = scanner.readLine();
        }
        System.out.println(tables);
        System.out.println();

        System.out.print("输入前缀(如：t_)：");
        String prefix = scanner.readLine();
        if (StrUtil.isBlank(prefix)) {
            prefix = "t_";
        }
        System.out.println(prefix);
        System.out.println();

        System.out.print("输入模块路径(如：biz/service)，没有就直接回车：");
        String moduleName = scanner.readLine();
        if (StrUtil.isBlank(moduleName)) {
            moduleName = "";
        }

        System.out.println(moduleName);
        System.out.println();


        System.out.print("输入包名(如：bailu.core.biz.base.system)：");
        String packageName = scanner.readLine();
        System.out.println(packageName);
        System.out.println();



        // 代码生成器对象
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置对象
        GlobalConfig gc = new GlobalConfig();
        // 项目地址
        String projectPath = System.getProperty("user.dir") + moduleName;
        // 生成路径
        gc.setOutputDir(projectPath+"/src/main/java");
        // 作者
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setServiceImplName("%sServiceImpl");
        // 是否开启swagger的注解
        gc.setSwagger2(true);
        gc.setIdType(IdType.ASSIGN_ID);


        mpg.setTemplateEngine(new VelocityTemplateEngine());
//        // 重点------------------------------------------------------------------
//        // 自定义模板配置
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController("template/guhong.controller.java")
                .setService("template/guhong.service.java")
                .setServiceImpl("template/guhong.serviceImpl.java");
        // 包中不生成xml文件，用我们自定义了xml的地址
        templateConfig.setXml(null);
//        // 重点------------------------------------------------------------------


        // 数据源配置对象，指定数据库



        // 包配置对象，用来配置包名，也就是说不需要自己创建包了
        final PackageConfig pc = new PackageConfig();
        // 设置包名。setParent设置父包名字，其他的是子包
        pc.setParent(packageName).
                setService("service").
                setMapper("mapper").
                setController("controller").
                setEntity("entity").
                setXml("mapper");


        // 自定义配置对象
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() { }
        };
        // 如果模板引擎是 velocity
         String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                String s = packageName.substring(packageName.lastIndexOf(".")+1);
                return projectPath + "/src/main/resources/mapper/" + s
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);


        // 策略配置对象
        StrategyConfig strategy = new StrategyConfig();
        // 下划线转驼峰命名
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 【实体】是否为lombok模型（默认 false）
        strategy.setEntityLombokModel(true);
        // 指定要生成的表
        strategy.setInclude(tables.split(","));
        // 去前缀
        strategy.setTablePrefix(prefix);
        // 是否使用RestController注解
        strategy.setRestControllerStyle(true);


        // 整合，设置到AutoGenerator中
        mpg.setGlobalConfig(gc);
        mpg.setDataSource(dsc);
        mpg.setPackageInfo(pc);
        mpg.setStrategy(strategy);
        mpg.setCfg(cfg);
        mpg.setTemplate(templateConfig);

        mpg.execute();
    }
}
