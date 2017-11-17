/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package manager;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DBCPUtil;
import util.DateUtil;
import util.LogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

/**
 * 数据库管理器
 * @author ruiying.hry
 * @version $Id: DatabaseManager.java, v 0.1 2017-11-16 上午11:21 ruiying.hry Exp $$
 */
public class DatabaseManager {

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    public DatabaseManager() {

    }

    /**
     *
     *
     * 插入sql,根据sql实现定制化插入
     *
     * sqlMap={"aa":"a","bb":"b"}
     *
     * 那么插入时将会生成sql如下:
     * INSERT INTO table
     * (aa,bb)
     * VALUES
     * (a,b)
     * @param table table  表名
     * @param sqlMap sqlMap key为列名,value为值(不需要带有gmt_create与gmt_modified)
     * @return 插入成功/失败
     */
    public boolean insert(String table, Map<String, Object> sqlMap) {

        Connection conn = DBCPUtil.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;

        if (conn == null) {
            LogUtil.error(logger, "get connection is null");
            return false;
        }

        boolean rst = true;

        try {

            //1. 创建insert语句
            StringBuilder sqlSb = new StringBuilder("INSERT INTO " + table + " (");
            StringBuilder keySb = new StringBuilder();
            StringBuilder valueSb = new StringBuilder();
            for (String key : sqlMap.keySet()) {
                keySb.append(key).append(",");

                Object value = sqlMap.get(key);
                if (value instanceof String) {
                    // > 数据库插入中,字符串有'标识包围
                    String val = new StringBuilder("\'").append(value).append("\'").toString();
                    valueSb.append(val);

                } else if (value instanceof Date) {
                    // >util.Date需要转化为sql.Date
                    valueSb.append("DATE_FORMAT(\'")
                        .append(DateUtil.format((Date) value, DateUtil.TIME_FORMAT_STANDARD))
                        .append("\',\'%Y-%m-%d %H:%i%s\')");
                } else {
                    valueSb.append(value);
                }
                valueSb.append(",");

            }

            //> 删除key列表和value列表末尾的 ,
            keySb.deleteCharAt(keySb.length() - 1);
            valueSb.deleteCharAt(valueSb.length() - 1);

            sqlSb.append(keySb).append(") VALUES (").append(valueSb).append(")");

            String sql = sqlSb.toString();

            LogUtil.debug(logger, "sql =" + sql);

            //2. 创建sql执行,并获取该插入的id,记录
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.execute();

            LogUtil.debug(logger, "sql insert success");

        } catch (MySQLIntegrityConstraintViolationException ex) {
            rst = false;
            LogUtil.debug(logger, "mysql constraint violation", ex);
        } catch (Exception e) {
            rst = false;
            LogUtil.error(logger, e, "insert failed.");
        } finally {
            DBCPUtil.releaseResource(rs, pst, conn);
        }

        return rst;
    }
}