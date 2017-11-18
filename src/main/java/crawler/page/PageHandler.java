package crawler.page;

/**
 * 爬虫都应该继承这个类进行处理
 * @author ruiying.hry
 * @version $Id: PageHandler.java, v 0.1 2017-11-15 下午7:00 ruiying.hry Exp $$
 */
public interface PageHandler {
    /**
     * 返回处理后的结果
     * @return 成功失败
     */
    boolean handle();
}