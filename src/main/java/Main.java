/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */

import crawler.page.PageHandleFactory;
import crawler.page.PageHandler;
import crawler.page.PageTypeEnum;
import manager.HttpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

/**
 * 入口
 * @author ruiying.hry
 * @version $Id: Main.java, v 0.1 2017-11-15 下午8:33 ruiying.hry Exp $$
 */
public class Main {

    /** 日志管理 */
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * 程序的主入口就在这
     * @param args
     */
    public static void main(String args[]) {

        //0. 进行一系列的初始
        HttpManager httpManager = new HttpManager();

        //1. 找到所有基金的列表
        try {
            LogUtil.infoCritical(logger, "START CRAWLER FUND CODE LIST");

            //1.1 获取html
            String fundAllGetUrl = "http://fund.eastmoney.com/js/fundcode_search.js";
            String htmlFundCode = httpManager.getHtmlByUrl(fundAllGetUrl);

            //1.2 处理信息
            PageHandler pageHandler = PageHandleFactory.newInstance(PageTypeEnum.FUND_ALL);
            pageHandler.handle(htmlFundCode);
            LogUtil.infoCritical(logger, "SUCCESS CRAWLER FUND CODE LIST");

        } catch (Exception e) {
            LogUtil.error(logger, e, "!!!!!!!!FUND CODE LIST FAILED!!!!!!!!!");
            httpManager.shuntDown();
        }

        //2. 处理每个基金的单独页面信息
    }
}