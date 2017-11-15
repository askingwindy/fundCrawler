/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ruiying.hry
 * @version $Id: DatabaseManager.java, v 0.1 2017-11-14 下午9:46 ruiying.hry Exp $$
 */
public class DatabaseManager {

    public static final String url      = "jdbc:mysql://127.0.0.1/"; //数据库连接
    public static final String name     = "com.mysql.jdbc.Driver";  //程序驱动
    public static final String user     = "root";                   //用户名
    public static final String password = "";                       //密码

    public Connection          conn     = null;
    public PreparedStatement   pst      = null;

    /**
     * 创建sql查询
     *
     * @param sql: SQL查询语句 
     */
    public DatabaseManager() {
        try {
            Class.forName(name);// 指定连接类型  
            conn = DriverManager.getConnection(url, user, password);// 获取连接  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 关闭数据库连接
     */
    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
