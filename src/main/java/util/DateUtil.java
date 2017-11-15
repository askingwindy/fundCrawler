package util;

import com.alibaba.common.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author ruiying.hry
 * @version $Id: DateUtil.java, v 0.1 2017-11-15 下午4:09 ruiying.hry Exp $$
 */
public abstract class DateUtil {

    /** 日志管理 */
    private static Logger      logger                   = LoggerFactory.getLogger(DateUtil.class);

    /**yyyy-MM-dd HH:mm:ss时间格式*/
    public final static String TIME_FORMAT_STANDARD     = "yyyy-MM-dd HH:mm:ss";

    /**yyyyMMddHHmmss时间格式*/
    public final static String TIME_FORMAT_NORMAL       = "yyyyMMddHHmmss";

    /**yyyyMMdd时间格式*/
    public final static String TIME_FORMAT_SHORT_TO_DAY = "yyyyMMdd";

    /**yyyy-MM-ddTHH:mm:ss.SSS时间格式*/
    public final static String TIME_FORMAT_ISO          = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    /**yyyy-MM-ddTHH:mm:ss时间格式*/
    public final static String TIME_FORMAT_ISO_SHORT    = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 将制定格式的时间戳转化为DATE
     * @param sDate 格式化前的日期
     * @return      格式化后的日期
     */
    public static Date parseDate(String sDate, String formatter) {

        if (StringUtil.isEmpty(sDate) || StringUtil.isEmpty(formatter)) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat(formatter);
        Date d = null;
        try {
            d = dateFormat.parse(sDate);
        } catch (ParseException ex) {
            LogUtil.error(logger, ex, "sDate=" + sDate + ", formatter=" + formatter + ",转化失败");
            return null;
        } catch (Exception ex) {
            LogUtil.error(logger, ex, "sDate=" + sDate + ", formatter=" + formatter + ",异常");
            return null;
        }

        return d;
    }

    /**
     * 时间格式
     * @param date      格式化前的时间
     * @param format    需要转换的格式
     * @return          格式化后的时间
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        return new SimpleDateFormat(format).format(date);
    }

}