import base.contants.ProxyTypeEnum;
import crawler.proxy.ProxyHandlerFactory;
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

        //1. 获取所有基金列表
        try {
            ProxyHandlerFactory.newInstance(ProxyTypeEnum.FUND_ALL).execute();
        } catch (Exception e) {
            LogUtil.error(logger,e);
        }

    }
}