package crawler.proxy;

/**
 *
 * @author ruiying.hry
 * @version $Id: ProxyHandler.java, v 0.1 2017-11-15 下午9:46 ruiying.hry Exp $$
 */
public interface ProxyHandler {

    /**
     * 返回处理后的结果
     * @return 成功失败
     */
    boolean execute();
}