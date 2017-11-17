package crawler.page.impl;

import base.contants.FundTableNameContants;
import base.enums.FundNavTableEnum;
import crawler.page.PageHandler;
import manager.DatabaseManager;
import util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtil;
import util.LogUtil;
import util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 获取基金净值页面的信息
 * 
 * 注:
 * 1. 首次通过pag=1,per=1可以获取所有净值的条数(这是最新的数据);然后请求pag=1,per=total,可以拿到所有数据
 * 2. TODO:如果以后做更新数据的插入,per设置为: total-数据库已有数据总和,设置per=datediff(),拿到条数
 *
 * @author ruiying.hry
 * @version $Id: FundNavPageHandler.java, v 0.1 2017-11-17 下午12:30 ruiying.hry Exp $$
 */
public class FundNavPageHandler implements PageHandler {

    /** 日志管理 */
    private static Logger       logger                = LoggerFactory
                                                          .getLogger(FundInfoPageHandler.class);

    /** 页面前缀*/
    private static final String FUND_NAV_PAGE_PREFIX  = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=";

    /** 查询条件的前缀*/
    private static final String FUND_NAV_QUERY_PREFIX = "&page=1&per=";

    private String              fundCode;

    @Override
    public boolean handle() {
        LogUtil.info(logger, "page handle fund NAVA start,code=" + fundCode);

        boolean rst = true;

        final DatabaseManager databaseManager = new DatabaseManager();
        String fundInfoHtml = "";
        try {

            //----------------------------解析html,得到需要获取数据的total----------------------------

            //0. 先拿到到最新的一条数据,可以拿到所有数据和
            String fundNavUrlLastInfo = FUND_NAV_PAGE_PREFIX + fundCode + FUND_NAV_QUERY_PREFIX
                                        + "1";
            fundInfoHtml = HttpUtils.getHtmlByUrl(fundNavUrlLastInfo);

            // > 1.1 得到total
            String recordCnt = fundInfoHtml.split("records:")[1].split(",")[0].trim();
            int total = Integer.parseInt(recordCnt);

            // > 1.2 判断这个是fund是否为货币基金
            Document documentTotalCnt = Jsoup.parse(fundInfoHtml);
            Elements totalCntEle = documentTotalCnt.select("table").select("th");
            LogUtil.debug(logger, totalCntEle.size());

            // > 1.3 fund的净值数据库表名
            String databaseTableName = "";
            if (totalCntEle.size() == 7) {
                //如果表格头长度为7,表明为非货币基金
                databaseTableName = FundTableNameContants.FUND_NAV_TALBE_NAME;
            } else if (totalCntEle.size() == 6) {
                //如果表格头长度为6,表明为货币基金
                databaseTableName = FundTableNameContants.FUND_CURRENCY_NAV_TALBE_NAME;
            } else {
                //抛错
                throw new RuntimeException("基金列表页面获取有误");
            }

            //2. 拿到数据库中这个fund_code的所有CNT技术
            int storedCnt = databaseManager.cntNavNumByFundCode(databaseTableName, fundCode);

            //3. 难道实际需要新增的Num
            int increaseCnt = total - storedCnt;

            if (increaseCnt <= 0) {
                //不需要新增,直接返回成功
                LogUtil.info(logger, "fundCode=" + fundCode + ",不需要新增数据,total=" + total
                                     + ",storedCnt=" + storedCnt + ",");
                return true;
            }

            //-----------------------------根据increaseCnt,拿到所有数据----------------------------

            //0. 爬取页面
            String fundNavUrl = FUND_NAV_PAGE_PREFIX + fundCode + FUND_NAV_QUERY_PREFIX
                                + String.valueOf(increaseCnt);
            LogUtil.debug(logger, fundNavUrl);
            fundInfoHtml = HttpUtils.getHtmlByUrl(fundNavUrl);

            Document documentNav = Jsoup.parse(fundInfoHtml);
            //1. 获得所有行的数据
            Elements fundNavTableValues = documentNav.select("table").select("tbody").select("tr");

            //>>>>开启多线程,进行数据库的写入工作
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);

            for (final Element line : fundNavTableValues) {
                final String tableName = databaseTableName;
                fixedThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        //2. 解析每一行的数据含义
                        Elements lineElements = line.getElementsByTag("td");
                        Map<String, Object> sqlObj = parseHtmlToDto(tableName, lineElements);

                        //3. 进行数据库插入操作
                        databaseManager.insert(tableName, sqlObj);
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

        } catch (Exception e) {
            throw new RuntimeException("http请求获取失败", e);
        } finally {
        }

        LogUtil.info(logger, "!!!page handle fund NAVA success,code=" + fundCode + "!!!");

        return rst;

    }

    /**
     * 解析每一行的元素,返回到dto的map中
     * @param databaseTableName 数据库表名,根据表名,解析方式不同
     * @param lineElements 每一行的elements
     * @return
     */
    private Map<String, Object> parseHtmlToDto(String databaseTableName, Elements lineElements) {
        Map<String, Object> sqlObj = new HashMap<String, Object>();

        sqlObj.put(FundNavTableEnum.FUND_CODE.getDbCode(), fundCode);

        if (StringUtils.equals(databaseTableName, FundTableNameContants.FUND_NAV_TALBE_NAME)) {
            //2.1 根据表名,判断为非货币基金
            Date recordDate = DateUtil.parseDate(lineElements.get(0).text(),
                DateUtil.TIME_FORMAT_STANDARD_TO_DAY);
            sqlObj.put(FundNavTableEnum.RECORD_DATE.getDbCode(), recordDate);

            String navStr = lineElements.get(1).text().trim();
            sqlObj.put(FundNavTableEnum.NAV.getDbCode(),
                Double.parseDouble(StringUtils.defaultBlank(navStr, "0.0")));

            String addNavStr = lineElements.get(2).text().trim();
            sqlObj.put(FundNavTableEnum.ADD_NAV.getDbCode(),
                Double.parseDouble(StringUtils.defaultBlank(addNavStr, "0.0")));

            String navChgRateStr = lineElements.get(3).text().split("%")[0];
            sqlObj.put(FundNavTableEnum.NAV_CHG_RATE.getDbCode(),
                Double.parseDouble(StringUtils.defaultBlank(navChgRateStr, "0.0")));

            sqlObj.put(FundNavTableEnum.BUY_STATE.getDbCode(), lineElements.get(4).text().trim());
            sqlObj.put(FundNavTableEnum.SELL_STATE.getDbCode(), lineElements.get(5).text().trim());
            sqlObj.put(FundNavTableEnum.DIV.getDbCode(), lineElements.get(6).text().trim());
        } else {
            //2.2 根据表名,判断为货币基金
            Date recordDate = DateUtil.parseDate(lineElements.get(0).text(),
                DateUtil.TIME_FORMAT_STANDARD_TO_DAY);
            sqlObj.put(FundNavTableEnum.RECORD_DATE.getDbCode(), recordDate);

            String profitUnitsStr = lineElements.get(1).text().trim();
            sqlObj.put(FundNavTableEnum.PROFIT_PER_UNITS.getDbCode(),
                Double.parseDouble(StringUtils.defaultBlank(profitUnitsStr, "0.0")));

            String profitRateStr = lineElements.get(2).text().split("%")[0];
            sqlObj.put(FundNavTableEnum.PROFIT_RATE.getDbCode(),
                Double.parseDouble(StringUtils.defaultBlank(profitRateStr, "0.0")));

            sqlObj.put(FundNavTableEnum.BUY_STATE.getDbCode(), lineElements.get(3).text().trim());
            sqlObj.put(FundNavTableEnum.SELL_STATE.getDbCode(), lineElements.get(4).text().trim());
            sqlObj.put(FundNavTableEnum.DIV.getDbCode(), lineElements.get(5).text().trim());
        }

        return sqlObj;

    }

    /**
     * Setter method for property   fundCode .
     *
     * @param fundCode value to be assigned to property fundCode
     */
    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}