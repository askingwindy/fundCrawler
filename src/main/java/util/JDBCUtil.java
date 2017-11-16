package util;

import base.contants.DatabaseParamsContants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 通过jdbc连接数据库
 * @author ruiying.hry
 * @version $Id: JDBCUtil.java, v 0.1 2017-11-14 9:46 ruiying.hry Exp $$
 */
public class JDBCUtil {

    /** 日志管理 */
    private static Logger     logger = LoggerFactory.getLogger(JDBCUtil.class);

    /** 连接*/
    private static Connection conn   = null;

    /**
     * 链接sql
     */
    public static void init() {
        LogUtil.debug(logger, "start database connecting");

        try {
            //1. 加载驱动
            Class.forName(DatabaseParamsContants.DRIVER_NAME);
            //2. 建立连接
            conn = DriverManager.getConnection(DatabaseParamsContants.URL,
                DatabaseParamsContants.USER, DatabaseParamsContants.PASSWORD);
        } catch (Exception e) {
            LogUtil.error(logger, e, "startdatabase connection failed");
        }
    }

    public static void close(){
        DBCPUtil.close();
    }

}
