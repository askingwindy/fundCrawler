package crawler.page;

import base.enums.PageTypeEnum;
import crawler.page.impl.FundCodePageHandler;
import crawler.page.impl.FundInfoPageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 页面处理工厂类
 *
 * 通过工厂生产页面处理产品,对不同的页面类型进行处理
 * @author ruiying.hry
 * @version $Id: PageHandleFactory.java, v 0.1 2017-11-15 下午6:55 ruiying.hry Exp $$
 */
public class PageHandleFactory {

    private static Map<PageTypeEnum, PageHandler> holder = new HashMap<PageTypeEnum, PageHandler>();

    static {
        holder.put(PageTypeEnum.FUND_INFO, new FundInfoPageHandler());
        holder.put(PageTypeEnum.FUND_ALL, new FundCodePageHandler());
    }

    /**
     * 根据类型返回结果
     * @param type
     * @return
     * @throws Exception
     */
    public static PageHandler newInstance(PageTypeEnum type) {
        return holder.get(type);
    }

}