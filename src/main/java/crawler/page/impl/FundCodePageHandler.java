package crawler.page.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import base.contants.FileNameContants;
import crawler.page.PageHandler;
import manager.FileManager;
import util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

/**
 * 获取基金所有codes的页面与处理
 * 
 * @author ruiying.hry
 * @version $Id: FundCodePageHandler.java, v 0.1 2017-11-15 下午7:51 ruiying.hry Exp $$
 */
public class FundCodePageHandler implements PageHandler {

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(FundCodePageHandler.class);

    /** 文件管理*/
    private FileManager   fileManager;

    @Override
    public boolean handle() {
        fileManager = new FileManager();

        try {

            //0. 爬取html
            String fundAllGetUrl = "http://fund.eastmoney.com/js/fundcode_search.js";
            String htmlFundCode = HttpUtils.getHtmlByUrl(fundAllGetUrl);

            //1. 解析html
            // > htmltext格式固定为:var r = [[
            // > arrStr行如:[["1", "2", "3"], ["4", "5", "6"] ]
            LogUtil.debug(logger, "arrStr=" + htmlFundCode);

            JSONArray jsonArray = JSON.parseArray(htmlFundCode);
            JSONArray fundCodeArray = new JSONArray();
            for (Object val : jsonArray) {
                // > val行如:["000001","HXCZ","华夏成长","混合型","HUAXIACHENGZHANG"],第一个字段为code
                JSONArray fundInfoArray = (JSONArray) val;
                fundCodeArray.add(fundInfoArray.get(0));
            }

            LogUtil.debug(logger, "fundCode=" + fundCodeArray.toString());

            //2. 放入到文本中持久化
            fileManager.writeIntoFile(FileNameContants.FUND_ALL_CODES_FILE,
                fundCodeArray.toJSONString(), false);

        } catch (Exception e) {
            throw new RuntimeException("获取基金页面处理失败", e);

        } finally {
            fileManager = null;
        }

        return true;
    }
}