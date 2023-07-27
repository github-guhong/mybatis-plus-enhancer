package guhong.play.mybatisplusenhancer.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本/字符工具类
 * @author 李双凯
 * @date 2019/9/21 16:21
 */
public class TextUtil {




    /**
     * 下划线转驼峰
     * @param str 下划线格式的字符串
     * @return 返回驼峰格式的字符串
     */
    public static String toCamel(String str) {
        //利用正则删除下划线，把下划线后一位改成大写
        Pattern pattern = Pattern.compile("_(\\w)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return toCamel(sb.toString());
    }

    /**
     * 驼峰转下划线
     * @param str 驼峰格式的字符串
     * @return 返回下划线格式
     */
    public static String toUnderline(String str) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if(matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb,"_"+matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        }else {
            return sb.toString();
        }
        return toUnderline(sb.toString());
    }





}
