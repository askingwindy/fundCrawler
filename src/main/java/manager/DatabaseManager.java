package manager;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;

/**
 *
 * @author ruiying.hry
 * @version $Id: DatabaseManager.java, v 0.1 2017-11-14 9:46 ruiying.hry Exp $$
 */
public class DatabaseManager {

    /** 日志管理 */
    private static Logger      logger   = LoggerFactory.getLogger(DatabaseManager.class);

    /** 连接地址*/
    public static final String url      = "jdbc:mysql://127.0.0.1/creep_fund";

    /** 驱动*/
    public static final String name     = "com.mysql.jdbc.Driver";

    /** 用户名*/
    public static final String user     = "root";

    /** 密码*/
    public static final String password = "hry123";

    /** 连接*/
    private Connection         conn     = null;

    /** pst*/
    private PreparedStatement  pst      = null;

    /**
     * 链接sql
     */
    public DatabaseManager() {
        logger.debug("start database connecting");

        try {
            //1. 加载驱动
            Class.forName(name);
            //2. 建立连接
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("startdatabase connection failed", e);
        }
    }

    /**
     * 关闭sql
     */
    public void close() {

        logger.debug("close database connecting");

        try {

            if (this.pst != null) {
                this.pst.close();
            }
            if (this.conn != null) {
                this.conn.close();

            }

        } catch (SQLException e) {

            logger.error("close database connection failed", e);
        }
    }

    /**
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
     *
     * @param table  表名
     * @param sqlMap key为列名,value为值(不需要带有gmt_create与gmt_modified)
     *
     * @return 插入成功/失败
     */
    public boolean insert(String table, Map<String, Object> sqlMap) {

        if (conn == null) {
            throw new RuntimeException("##DatabaseManager##mysql connection is null");
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
                    String val = new StringBuilder("\'").append(value).append("\'").toString();
                    valueSb.append(val);

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

            logger.debug("##DatabaseManager##sql =" + sql);

            //2. 创建sql执行,并获取该插入的id,记录
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.execute();

            ResultSet rs = pst.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            logger.debug("##DatabaseManager##sql insert success, id=" + id);

        } catch (MySQLIntegrityConstraintViolationException ex) {
            rst = false;
            logger.error("##DatabaseManager##mysql constraint violation", ex);
        } catch (Exception e) {
            rst = false;
            logger.error("##DatabaseManager##insert failed.", e);
        }

        return rst;
    }
}
