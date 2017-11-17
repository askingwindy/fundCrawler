package crawler.proxy;

import base.enums.ProxyTypeEnum;
import crawler.proxy.impl.FundCodeProxyHandler;
import crawler.proxy.impl.FundInfoProxyHandler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ruiying.hry
 * @version $Id: ProxyHandlerFactory.java, v 0.1 2017-11-15 下午9:46 ruiying.hry Exp $$
 */
public class ProxyHandlerFactory {
    private static Map<ProxyTypeEnum, ProxyHandler> holder = new HashMap<ProxyTypeEnum, ProxyHandler>();

    static {
        holder.put(ProxyTypeEnum.FUND_ALL, new FundCodeProxyHandler());
        holder.put(ProxyTypeEnum.FUND_INFO, new FundInfoProxyHandler());
    }

    /**
     * 根据类型返回结果
     * @param type
     * @return
     * @throws Exception
     */
    public static ProxyHandler newInstance(ProxyTypeEnum type) throws Exception {
        return holder.get(type);
    }
}