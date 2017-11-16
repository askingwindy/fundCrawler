package util;

import base.contants.DatabaseParamsContants;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 通过jbcp管理
 * @author ruiying.hry
 * @version $Id: DBCPUtil.java, v 0.1 2017-11-16 上午10:38 ruiying.hry Exp $$
 */
public class DBCPUtil {
    /** 日志工具*/
    private static Logger          logger          = LoggerFactory.getLogger(JDBCUtil.class);

    /** 数据源,单例*/
    private static BasicDataSource basicDataSource = null;

    /** 创建数据源 */
    static {
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(DatabaseParamsContants.DRIVER_NAME);
        basicDataSource.setUrl(DatabaseParamsContants.URL);
        basicDataSource.setUsername(DatabaseParamsContants.USER);
        basicDataSource.setPassword(DatabaseParamsContants.PASSWORD);
        //数据库初始化时，创建的连接个数
        basicDataSource.setInitialSize(100);
        basicDataSource.setMaxTotal(100);
        //最小空闲连接数:当空闲的连接数少于阀值时，连接池就会预申请去一些连接，以免洪峰来时来不及申请。
        basicDataSource.setMinIdle(10);
        //数据库最大排队连接数
        basicDataSource.setMaxIdle(1000);
        //最大等待时间:1s
        basicDataSource.setMaxWaitMillis(1000);
        //空闲连接60秒中后释放
        basicDataSource.setMinEvictableIdleTimeMillis( 1000);
        //5分钟检测一次是否有死掉的线程
        basicDataSource.setTimeBetweenEvictionRunsMillis(1000);
        //回收被遗弃的链接
        basicDataSource.setRemoveAbandonedTimeout(30);
    }

    /**
     * 释放数据源
     */
    public static void close() {
        if (basicDataSource != null) {
            try {
                basicDataSource.close();
            } catch (Exception e) {
                LogUtil.error(logger, e);
            }
        }
    }

    /**
     * 从连接池中获取数据库连接
     * @return
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            con = basicDataSource.getConnection();
        } catch (Exception e) {
            LogUtil.error(logger, e);
        }
        return con;
    }

    /**
     * 关闭连接
     * 此时连接并不是与mysql关闭,而是被回收到连接池中
     */
    public static void releaseResource(ResultSet rs, PreparedStatement ps, Connection con) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                LogUtil.error(logger, e, "关闭结果集ResultSet异常！");
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                LogUtil.error(logger, e, "预编译SQL语句对象PreparedStatement关闭异常！");
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                LogUtil.error(logger, e, "关闭连接对象Connection异常！");
            }
        }
    }
}