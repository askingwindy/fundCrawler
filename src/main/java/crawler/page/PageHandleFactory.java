/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package crawler.page;

import base.enums.PageTypeEnum;
import crawler.page.impl.FundInfoPageHandler;
import crawler.page.impl.FundNavPageHandler;
import crawler.page.impl.FunderInfoPageHandler;

/**
 * 做一个页面工程进行 处理
 * @author ruiying.hry
 * @version $Id: PageHandleFactory.java, v 0.1 2017-11-17 下午12:26 ruiying.hry Exp $$
 */
public class PageHandleFactory {

    /**
     * 根据传入的PageType类型生产相关产品
     * @param pageTypeEnum
     * @return
     */
    public PageHandler getInstance(PageTypeEnum pageTypeEnum) {
        switch (pageTypeEnum) {
            case FUND_INFO:
                return new FundInfoPageHandler();
            case FUND_MANAGER:
                return new FunderInfoPageHandler();
            case FUND_NAV:
                return new FundNavPageHandler();
            default:
                throw new RuntimeException("没有这个type存在,pageType=" + pageTypeEnum);
        }
    }
}