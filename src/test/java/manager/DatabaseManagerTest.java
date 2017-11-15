package manager;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruiyingHe on 2017/11/15.
 */
public class DatabaseManagerTest {



    @Test
    public void testInsert() throws Exception {
        DatabaseManager dbManager = new DatabaseManager();
        try {
            Map<String, Object> sqlMap = new HashMap<String, Object>();
            sqlMap.put("fund_name", "基金名字");
            sqlMap.put("fund_abbr_name", "基金简称");
            sqlMap.put("fund_code", "test10000002");
            sqlMap.put("fund_type", "编造的");
            sqlMap.put("issue_date", "2017-11-11");
            sqlMap.put("establish_date", "test1");
            sqlMap.put("establish_scale", "test2");
            sqlMap.put("asset_value", "test3");
            sqlMap.put("asset_value_date", "test4");
            sqlMap.put("units", "test5");
            sqlMap.put("units_date", "test6");
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

            dbManager.insert("fund_info", sqlMap);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            dbManager.close();
        }
    }
}