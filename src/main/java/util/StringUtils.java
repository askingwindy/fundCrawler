package util;

import java.util.Collection;

/**
 *
 * @author ruiying.hry
 * @version $Id: StringUtils.java, v 0.1 2017-11-16 下午12:47 ruiying.hry Exp $$
 */
public class StringUtils {

    public static final String SEPARATOR = ",";

    /**
     *  将list 按照逗号分隔,输出为字符串(最后一位为,)
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Collection> String list2Str(T list) {
        StringBuilder sb = new StringBuilder();
        for (Object key : list) {
            sb.append(key.toString()).append(StringUtils.SEPARATOR);
        }
        return sb.toString();
    }

    /**
     * 判断str1与str2是否相等
     *
     * StringUtil.equals(null, null)   = true
     * StringUtil.equals(null, "abc")  = false
     * StringUtil.equals("abc", null)  = false
     * StringUtil.equals("abc", "abc") = true
     * StringUtil.equals("abc", "ABC") = false
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    /**
     * 判断str是否为空
     *
     * StringUtil.isEmpty(null)      = true
     * StringUtil.isEmpty("")        = true
     * StringUtil.isEmpty(" ")       = false
     * StringUtil.isEmpty("bob")     = false
     * StringUtil.isEmpty("  bob  ") = false
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
}