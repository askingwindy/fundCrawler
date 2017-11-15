/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package crawler.page;

/**
 * 爬虫都应该继承这个类进行处理
 * @author ruiying.hry
 * @version $Id: PageHandler.java, v 0.1 2017-11-15 下午7:00 ruiying.hry Exp $$
 */
public interface PageHandler {
    /**
     * 返回处理后的结果
     * @param htmltext 页面信息
     * @return 成功失败
     */
    boolean getResult(String htmltext);
}