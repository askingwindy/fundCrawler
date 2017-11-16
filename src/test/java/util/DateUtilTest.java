package util;

import org.junit.Test;

/**
 * Created by ruiyingHe on 2017/11/16.
 */
public class DateUtilTest {

    @Test
    public void testParseDate() throws Exception {
        String value="2.21亿元（截止至：2017年11月10日）";

        value = value.split("（截止至：")[1];
        value= value.substring(0,value.length()-1);

        System.out.print(DateUtil.parseDate(value));
    }
}