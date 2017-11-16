package crawler.proxy.impl;

import base.Result;
import base.enums.PageTypeEnum;
import base.template.ServiceCallBack;
import base.template.ServiceTemplate;
import base.template.impl.ServiceTemplateImpl;
import crawler.page.PageHandleFactory;
import crawler.page.PageHandler;
import crawler.proxy.ProxyHandler;
import manager.HttpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

/**
 *
 * @author ruiying.hry
 * @version $Id: FundCodeProxyImpl.java, v 0.1 2017-11-15 下午9:48 ruiying.hry Exp $$
 */
public class FundCodeProxyImpl implements ProxyHandler {

    /** 日志管理 */
    private static Logger   logger          = LoggerFactory.getLogger(FundCodeProxyImpl.class);

    /** 模板处理*/
    private ServiceTemplate serviceTemplate = new ServiceTemplateImpl();

    @Override
    public boolean execute() {
        final Result<Void> rst = new Result<Void>();

        serviceTemplate.executeWithoutTransaction(rst, new ServiceCallBack() {
            HttpManager httpManager;

            @Override
            public void before() {
                httpManager = new HttpManager();
            }

            @Override
            public void executeService() {

                //1. 获取html
                String fundAllGetUrl = "http://fund.eastmoney.com/js/fundcode_search.js";
                String htmlFundCode = httpManager.getHtmlByUrl(fundAllGetUrl);

                //2. 处理信息
                PageHandler pageHandler = PageHandleFactory.newInstance(PageTypeEnum.FUND_ALL);
                pageHandler.handle(htmlFundCode);
                LogUtil.infoCritical(logger, "SUCCESS CRAWLER FUND CODE LIST");

            }

            @Override
            public void end() {
                httpManager.shuntDown();

            }
        });

        return rst.isSuccess();
    }
}