package guhong.play.mybatisplusenhancer.constants.enums;

/**
 * 排序类型枚举
 * @author 李双凯
 * @date 2019/10/31 20:33
 */
public enum OrderType {
    /**
     * 降序排序
     */
    DESC("desc"),
    /**
     * 升序排序
     */
    ASC("asc")
    ;

    private String typeName;

    OrderType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static boolean isAsc(String way) {
        return ASC.typeName.equals(way);
    }
}
