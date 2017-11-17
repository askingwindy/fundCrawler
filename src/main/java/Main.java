import base.contants.FileNameContants;
import base.enums.PageTypeEnum;
import base.enums.ProxyTypeEnum;
import crawler.proxy.ProxyHandlerFactory;
import crawler.proxy.impl.FundInfoProxyHandler;
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

        try {
            //1. 获取所有基金列表
            //            ProxyHandlerFactory.newInstance(ProxyTypeEnum.FUND_ALL).execute();

            //2. 获取基金单个页面详情
            FundInfoProxyHandler fundInfoProxyHandler = (FundInfoProxyHandler) ProxyHandlerFactory
                .newInstance(ProxyTypeEnum.FUND_INFO);
            fundInfoProxyHandler.initial(LoggerFactory.getLogger("fund_info_error_code"),
                LoggerFactory.getLogger("fund_info_success_code"),
                FileNameContants.FUND_INFO_HANDLED_CODES_FILE, PageTypeEnum.FUND_INFO);
            fundInfoProxyHandler.execute();

        } catch (Exception e) {
            LogUtil.error(logger, e);
        } finally {
            //子线程还在运行,主线程就跑到这儿了,为啥捏
            //            DBCPUtil.close();
        }

    }
}