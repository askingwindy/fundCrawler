/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */

import crawler.page.PageHandleFactory;
import crawler.page.PageHandler;
import crawler.page.PageTypeEnum;
import util.HttpUtil;

/**
 *
 * @author ruiying.hry
 * @version $Id: Main.java, v 0.1 2017-11-15 下午8:33 ruiying.hry Exp $$
 */
public class Main {

    /**
     * 程序的主入口就在这
     * @param args
     */
    public static void main(String args[]){

        //0. 进行一系列的初始



        //1. 先找到所有基金的列表,并放入到文件中
        try {
            String fundAllGetUrl = "http://fund.eastmoney.com/js/fundcode_search.js";
            PageHandler pageHandler = PageHandleFactory.newInstance(PageTypeEnum.FUND_ALL);

            pageHandler.getResult(HttpUtil.getHtmlByUrl(fundAllGetUrl));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}