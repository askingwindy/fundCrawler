package manager;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DBCPUtil;
import util.DateUtil;
import util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruiyingHe on 2017/11/15.
 */
public class DatabaseManagerTest {


    /**日志管理-成功写入code的输出*/
    private static Logger sueedssCodeLogger = LoggerFactory.getLogger("fund_info_success_code");

    @Test
    public void testInsert() throws Exception {
        DatabaseManager dbManager = new DatabaseManager(DBCPUtil.getConnection());
        LogUtil.info(sueedssCodeLogger,",");

        try {
            Map<String, Object> sqlMap = new HashMap<String, Object>();
            sqlMap.put("fund_name", "基金名字");
            sqlMap.put("fund_abbr_name", "基金简称");
            sqlMap.put("fund_code", "test10000005");
            sqlMap.put("fund_type", "编造的");
            sqlMap.put("issue_date", DateUtil.parseDate("2017-11-11 00:00:00",DateUtil.TIME_FORMAT_STANDARD));
            sqlMap.put("establish_scale", "test2");
            sqlMap.put("asset_value", "test3");
            sqlMap.put("units", "test5");
            sqlMap.put("fund_manager", "test7");
            sqlMap.put("fund_trustee", "test8");
            sqlMap.put("funder", "test9");
            sqlMap.put("total_div", "test10");
            sqlMap.put("mgt_fee", "test11");
            sqlMap.put("trust_fee", "test12");
            sqlMap.put("sale_fee", "test13");
            sqlMap.put("buy_fee", "test14");
            sqlMap.put("buy_fee2", "test15");
            sqlMap.put("benchmark", "test16");
            sqlMap.put("underlying", "test17");

            System.out.println(sqlMap);

            dbManager.insert("fund_info", sqlMap);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}