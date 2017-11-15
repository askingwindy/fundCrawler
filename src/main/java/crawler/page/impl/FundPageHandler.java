package crawler.page.impl;

import crawler.page.PageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * 爬虫类的主体,利用开源java爬虫框架(Crawler4j)实现
 * @author ruiying.hry
 * @version $Id: FundPageHandler.java, v 0.1 2017-11-15 下午6:01 ruiying.hry Exp $$
 */
public class FundPageHandler implements PageHandler {
    /** 日志管理 */
    private static Logger        logger  = LoggerFactory.getLogger(FundPageHandler.class);

    /** */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp3|zip|gz))$");

    @Override
    public boolean handle(String htmltext) {
        return false;
    }
}