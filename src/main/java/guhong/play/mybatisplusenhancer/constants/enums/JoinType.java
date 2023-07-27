package guhong.play.mybatisplusenhancer.constants.enums;

/**
 * 表连接类型
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public enum  JoinType {

    /**
     * 左连接
     */
    LEFT("left"),
    /**
     * 右连接
     */
    RIGHT("right"),

    /**
     * 全连接
     */
    INNER("inner");

    private String value;

    JoinType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
