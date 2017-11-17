package crawler.proxy.impl;

import base.Result;
import base.contants.FileNameContants;
import base.template.ServiceCallBack;
import base.template.ServiceTemplate;
import base.template.impl.ServiceTemplateImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 基金单个页面详情获取
 *
 * @author ruiying.hry
 * @version $Id: FundInfoProxyImpl.java, v 0.1 2017-11-16 上午11:30 ruiying.hry Exp $$
 */
public class FundInfoProxyImpl implements ProxyHandler {

    /**日志管理*/
    private static Logger   logger            = LoggerFactory.getLogger(FundInfoProxyImpl.class);

    /**日志管理-错误code的输出*/
    private static Logger   errorCodeLogger   = LoggerFactory.getLogger("fund_info_error_code");

    /**日志管理-成功写入code的输出*/
    private static Logger   sueedssCodeLogger = LoggerFactory.getLogger("fund_info_success_code");

    /**
     * 模板处理
     */
    private ServiceTemplate serviceTemplate   = new ServiceTemplateImpl();

    @Override
    public boolean execute() {

        final Result<Void> rst = new Result<Void>();

        final List<String> toHandledCodes = new Vector<String>();

        serviceTemplate.executeWithoutTransaction(rst, new ServiceCallBack() {

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
                    if (StringUtils.isEmpty(code)) {
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

                LogUtil.infoCritical(logger, "TOTAL FUND CNT:" + toHandledCodes.size());

                ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);

                //1. 获取html
                for (final String fundCode : toHandledCodes) {
                    if (StringUtils.isEmpty(fundCode)) {
                        //容错一下
                        continue;
                    }

                    fixedThreadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LogUtil.info(logger, "start handling fundCode=" + fundCode);
                                HttpManager httpManager = new HttpManager();
                                String fundInfoUrl = "http://fund.eastmoney.com/f10/jbgk_"
                                                     + fundCode + ".html";

                                String fundInfoHtml = httpManager.getHtmlByUrl(fundInfoUrl);
                                httpManager.shuntDown();

                                //2. 处理信息
                                PageHandler pageHandler = new FundInfoPageHandler();
                                if (pageHandler instanceof FundInfoPageHandler) {
                                    ((FundInfoPageHandler) pageHandler).setFundCode(fundCode);
                                }
                                if (pageHandler.handle(fundInfoHtml)) {
                                    //2.1 如果处理成功了,将这个code放入到已处理的set中
                                    LogUtil.info(sueedssCodeLogger, fundCode + ",");
                                } else {
                                    //2.2 如果失败了,在控制台输出记录一下
                                    LogUtil.warn(logger, "fund info handle failed, code="
                                                         + fundCode);
                                    throw new RuntimeException("code insert database failed");
                                }
                            } catch (Exception ex) {
                                LogUtil.error(logger,ex,"fundCode="+fundCode);
                                LogUtil.info(errorCodeLogger, fundCode + ",");
                            }

                        }
                    });
                }

                // 启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
                fixedThreadPool.shutdown();
                try {
                    // 请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行
                    // 设置最长等待10秒
                    while (!fixedThreadPool.awaitTermination(10, TimeUnit.SECONDS))
                        ;
                } catch (InterruptedException e) {
                    //
                    LogUtil.error(logger, e);
                }

                LogUtil.infoCritical(logger, "SUCCESS END CRAWLER FUND INFO");

            }

            @Override
            public void end() {
                fileManager = null;
            }
        });

        return rst.isSuccess();
    }
}