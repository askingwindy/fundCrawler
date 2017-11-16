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
}