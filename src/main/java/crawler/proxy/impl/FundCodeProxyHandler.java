package crawler.proxy.impl;

import base.Result;
import base.template.ServiceCallBack;
import base.template.ServiceTemplate;
import base.template.impl.ServiceTemplateImpl;
import crawler.page.PageHandler;
import crawler.page.impl.FundCodePageHandler;
import crawler.proxy.ProxyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

/**
 *
 * @author ruiying.hry
 * @version $Id: FundCodeProxyHandler.java, v 0.1 2017-11-15 下午9:48 ruiying.hry Exp $$
 */
public class FundCodeProxyHandler implements ProxyHandler {

    /** 日志管理 */
    private static Logger   logger          = LoggerFactory.getLogger(FundCodeProxyHandler.class);

    /** 模板处理*/
    private ServiceTemplate serviceTemplate = new ServiceTemplateImpl();

    @Override
    public boolean execute() {
        final Result<Void> rst = new Result<Void>();

        serviceTemplate.executeWithoutTransaction(rst, new ServiceCallBack() {

            @Override
            public void before() {

            }

            @Override
            public void executeService() {

                PageHandler pageHandler = new FundCodePageHandler();
                pageHandler.handle();
                LogUtil.infoCritical(logger, "SUCCESS CRAWLER FUND CODE LIST");

            }

            @Override
            public void end() {

            }
        });

        return rst.isSuccess();
    }
}