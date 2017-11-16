package crawler.proxy.impl;

import base.Result;
import base.contants.FileNameContants;
import base.enums.PageTypeEnum;
import base.template.ServiceCallBack;
import base.template.ServiceTemplate;
import base.template.impl.ServiceTemplateImpl;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import crawler.page.PageHandleFactory;
import crawler.page.PageHandler;
import crawler.page.impl.FundInfoPageHandler;
import crawler.proxy.ProxyHandler;
import manager.FileManager;
import manager.HttpManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;
import util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基金单个页面详情获取
 *
 * @author ruiying.hry
 * @version $Id: FundInfoProxyImpl.java, v 0.1 2017-11-16 上午11:30 ruiying.hry Exp $$
 */
public class FundInfoProxyImpl implements ProxyHandler {

    /**
     * 日志管理
     */
    private static Logger   logger          = LoggerFactory.getLogger(FundInfoProxyImpl.class);

    /**
     * 模板处理
     */
    private ServiceTemplate serviceTemplate = new ServiceTemplateImpl();

    @Override
    public boolean execute() {

        final Result<Void> rst = new Result<Void>();

        final List<String> handleSuccessCodes = new ArrayList<String>();
        final List<String> toHandledCodes = new ArrayList<String>();

        serviceTemplate.executeWithoutTransaction(rst, new ServiceCallBack() {

            HttpManager httpManager = new HttpManager(); ;
            FileManager fileManager = new FileManager();

            @Override
            public void before() {
                //1. 从fundCodeList得到所有基金列表A
                String fundCodesStr = fileManager.readFile(FileNameContants.FUND_ALL_CODES);
                JSONArray allCodes = JSON.parseArray(fundCodesStr);

                List<String> allCodesSet = new ArrayList<String>();
                for (Object code : allCodes) {
                    allCodesSet.add(String.valueOf(code));
                }

                //2. 从fundeInfoHandleList里面得到信息已经被处理的基金列表B
                String infoHandledCodesStr = fileManager
                    .readFile(FileNameContants.FUND_INFO_HANDLED_CODES);
                String[] infohandledCodes = infoHandledCodesStr.split(",");

                List<String> handledCodesSet = new ArrayList<String>();
                for (String code : infohandledCodes) {
                    if (StringUtil.isEmpty(code)) {
                        continue;
                    }
                    handledCodesSet.add(String.valueOf(code));
                }

                //3. 上述两个set:A与非B做交集,得到待处理的基金
                for (String code : allCodesSet) {
                    if (handledCodesSet.contains(code)) {
                        //3.1 如果已经处理了,那么不做处理
                        continue;
                    }
                    //3.2 如果还没有处理,那么放入到set中等待处理
                    toHandledCodes.add(code);
                }
            }

            @Override
            public void executeService() {

                ExecutorService cachedThreadPool =  Executors.newFixedThreadPool(4);;

                //1. 获取html
                for (final String fundCode : toHandledCodes) {
                    if (StringUtil.isEmpty(fundCode)) {
                        //容错处理一下
                        continue;
                    }

                    cachedThreadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            String fundInfoUrl = "http://fund.eastmoney.com/f10/jbgk_" + fundCode
                                                 + ".html";

                            String fundInfoHtml = httpManager.getHtmlByUrl(fundInfoUrl);

                            //2. 处理信息
                            PageHandler pageHandler = PageHandleFactory
                                .newInstance(PageTypeEnum.FUND_INFO);
                            if (pageHandler instanceof FundInfoPageHandler) {
                                ((FundInfoPageHandler) pageHandler).setFundCode(fundCode);
                            }
                            if (pageHandler.handle(fundInfoHtml)) {
                                //2.1 如果处理成功了,将这个code放入到已处理的set中
                                handleSuccessCodes.add(fundCode);
                            } else {
                                //2.2 如果失败了,在控制台输出记录一下
                                LogUtil.info(logger, "fund info handle failed, code=" + fundCode);
                            }
                        }
                    });

                }
                LogUtil.infoCritical(logger, "SUCCESS CRAWLER FUND CODE LIST");

            }

            @Override
            public void end() {
                httpManager.shuntDown();
                //将本次处理的基金写入到文件中,hanndledCodes写入到文件中(在文末追加)
                fileManager.writeIntoFile(FileNameContants.FUND_INFO_HANDLED_CODES,
                    StringUtils.list2Str(handleSuccessCodes), true);
                fileManager = null;

            }
        });

        return rst.isSuccess();
    }
}