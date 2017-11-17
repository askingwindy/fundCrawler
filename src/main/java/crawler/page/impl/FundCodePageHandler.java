package crawler.page.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import base.contants.FileNameContants;
import crawler.page.PageHandler;
import manager.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

/**
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
    public boolean handle(String htmltext) {
        //1. 解析html
        // > htmltext格式固定为:var r = [[
        String arrStr = htmltext.substring(8, htmltext.length() - 1).trim();
        // > arrStr行如:[["1", "2", "3"], ["4", "5", "6"] ]
        LogUtil.debug(logger, "arrStr=" + arrStr);

        JSONArray jsonArray = JSON.parseArray(arrStr);

        JSONArray fundCodeArray = new JSONArray();
        for (Object val : jsonArray) {
            // > val行如:["000001","HXCZ","华夏成长","混合型","HUAXIACHENGZHANG"],第一个字段为code
            JSONArray fundInfoArray = (JSONArray) val;
            fundCodeArray.add(fundInfoArray.get(0));
        }

        LogUtil.debug(logger, "fundCode=" + fundCodeArray.toString());

        //2. 放入到文本中持久化
        fileManager = new FileManager();
        fileManager.writeIntoFile(FileNameContants.FUND_ALL_CODES_FILE, fundCodeArray.toJSONString(),
            false);
        fileManager = null;

        return true;
    }
}