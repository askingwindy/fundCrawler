package util;

import org.slf4j.Logger;

/**
 * 日志工具
 * @author ruiying.hry
 * @version $Id: LogUtil.java, v 0.1 2017-11-15 下午9:08 ruiying.hry Exp $$
 */
public class LogUtil {
    /**格式常量*/
    private static final String BANNER = "===============================================";

    /**
     * info等级,输出critical格式
     * =======
     * xxx
     * =======
     * @param logger
     * @param obj
     */
    public static void infoCritical(Logger logger, Object... obj) {
        if (logger == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(BANNER);
            logger.info(getLogString(obj));
            logger.info(BANNER);
        }
    }

    /**
     * 生成警告级别日志
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger    日志对象
     * @param throwable 异常栈对象
     * @param obj       泛型对象列表
     */
    public static void warn(Logger logger, Throwable throwable, Object... obj) {
        if (logger != null) {
            logger.warn(getLogString(obj), throwable);
        }
    }

    /**
     * 生成错误级别日志
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger    日志对象
     * @param throwable 异常栈对象
     * @param obj       泛型对象列表
     */
    public static void error(Logger logger, Throwable throwable, Object... obj) {
        if (logger != null) {
            logger.error(getLogString(obj), throwable);
        }
    }

    /**
     * 生成错误级别日志
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger    日志对象
     * @param obj       泛型对象列表
     */
    public static void error(Logger logger, Object... obj) {
        if (logger != null) {
            logger.error(getLogString(obj));
        }
    }

    /**
     * 生成<font color="blue">调试</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger    日志对象
     * @param obj       泛型对象列表
     */
    public static void debug(Logger logger, Object... obj) {
        if (logger == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(getLogString(obj));
        }
    }

    /**
     * 生成<font color="blue">通知</font>级别日志<br>
     * 可处理任意多个输入参数，并避免在日志级别不够时字符串拼接带来的资源浪费
     *
     * @param logger    日志对象
     * @param obj       泛型对象列表
     */
    public static void info(Logger logger, Object... obj) {
        if (logger == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(getLogString(obj));
        }
    }

    /**
     * 生成输出到日志的字符串
     *
     * @param obj    泛型对象列表
     * @return       输出对象序列化字符串
     */
    private static String getLogString(Object... obj) {
        StringBuilder log = new StringBuilder();
        for (Object o : obj) {
            log.append(o);
        }
        return log.toString();
    }

}