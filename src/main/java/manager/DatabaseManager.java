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
 * @version $Id: DatabaseManager.java, v 0.1 2017-11-14 ����9:46 ruiying.hry Exp $$
 */
public class DatabaseManager {

    public static final String url      = "jdbc:mysql://127.0.0.1/"; //���ݿ�����
    public static final String name     = "com.mysql.jdbc.Driver";  //��������
    public static final String user     = "root";                   //�û���
    public static final String password = "";                       //����

    public Connection          conn     = null;
    public PreparedStatement   pst      = null;

    /**
     * ����sql��ѯ
     *
     * @param sql: SQL��ѯ��� 
     */
    public DatabaseManager() {
        try {
            Class.forName(name);// ָ����������  
            conn = DriverManager.getConnection(url, user, password);// ��ȡ����  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * �ر����ݿ�����
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
