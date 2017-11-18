package crawler.page.impl;

import base.contants.FundTableNameContants;
import base.enums.FundInfoTableMappingEnum;
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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 获取单个基金详情页面的爬取与处理
 * 
 * @author ruiying.hry
 * @version $Id: FundInfoPageHandler.java, v 0.1 2017-11-15 下午6:01 ruiying.hry Exp $$
 */
public class FundInfoPageHandler implements PageHandler {
    /** 日志管理 */
    private static Logger       logger                = LoggerFactory
                                                          .getLogger(FundInfoPageHandler.class);

    private static final String FUND_INFO_PAGE_PREFIX = "http://fund.eastmoney.com/f10/jbgk_";

    private static final String FUND_INFO_PAGE_SUFFIX = ".html";

    private String              fundCode;

    @Override
    public boolean handle() {

        LogUtil.info(logger, "page handle fund start,code=" + fundCode);

        boolean rst = true;

        if (StringUtils.isEmpty(fundCode)) {
            throw new RuntimeException("基金代码不存在,无法执行逻辑");
        }

        //0. 准备map,这个用于写入数据库中
        Map<String, Object> fundInfo = new HashMap<String, Object>();

        //1. 爬取网页数据,从html的选择器中拿到table显示的数据
        String fundInfoHtml = getHtml();

        //2. 解析网页
        Document doc = Jsoup.parse(fundInfoHtml);

        Elements fundTableElement = doc.select("table.info").select(".w790");

        //2.1 通过映射,找到对应的枚举
        for (Element el : fundTableElement.select("th")) {
            FundInfoTableMappingEnum mappingEnum = FundInfoTableMappingEnum.getEnumByChName(el
                .text());

            if (mappingEnum == null) {
                continue;
            }

            this.handleVaule(fundInfo, mappingEnum, el.nextElementSibling().text());
        }
        fundInfo.put(FundInfoTableMappingEnum.FUND_CODE.getDbCode(), fundCode);

        LogUtil.debug(logger, fundInfo.toString());

        //3. 数据库处理
        try {
            LogUtil.debug(logger, fundInfo.toString());
            DatabaseManager databaseManager = new DatabaseManager();
            rst = databaseManager.insert(FundTableNameContants.FUND_INFO_TALBE_NAME, fundInfo);
        } catch (Exception e) {
            LogUtil.error(logger, e, "handle fund info failed, code=" + fundCode);
            return false;
        }

        LogUtil.info(logger, "handle fund success,code=" + fundCode);

        return rst;
    }

    /**
     * 爬取网页数据
     * @return 网页内容
     */
    private String getHtml() {
        //http请求

        String fundInfoHtml = null;
        try {
            String fundInfoUrl = FUND_INFO_PAGE_PREFIX + fundCode + FUND_INFO_PAGE_SUFFIX;

            fundInfoHtml = HttpUtils.getHtmlByUrl(fundInfoUrl);
        } catch (Exception e) {
            throw new RuntimeException("http请求获取失败", e);
        } finally {
        }
        return fundInfoHtml;
    }

    /**
     * 处理value,并放入到map中
     * @param fundInfo 需要写入到数据库的Map
     * @param mappingEnum 映射枚举
     * @param value 值
     * @return
     */
    private Object handleVaule(Map<String, Object> fundInfo, FundInfoTableMappingEnum mappingEnum,
                               String value) {
        Object rst = new Object();

        switch (mappingEnum) {
            case ISSUE_DATE:
                rst = DateUtil.parseDate(value.trim());
                break;
            case ESTABLISH_DATE:
                //这个类型,需要处理两个值,scale先写入到map中
                rst = DateUtil.parseDate(value.split("/")[0].trim());

                //发行规模
                if (value.contains("/")) {
                    value = value.split("/")[1].trim();
                } else {
                    value = "---";
                }
                fundInfo.put(FundInfoTableMappingEnum.ESTABLISH_SCALE.getDbCode(), value);
                break;
            case ASSET_VALUE:
                rst = value.split("（截止至")[0].trim();

                //资产统计截止日期
                value = this.getDate(value, "（截止至：", 1);
                fundInfo.put(FundInfoTableMappingEnum.ASSET_VALUE_DATE.getDbCode(),
                    DateUtil.parseDate(value.substring(0, value.length() - 1)));
                break;
            case UNITS:
                //2.2736亿份（截止至：2017年09月30日）
                rst = value.split("（")[0];

                //份额统计最后日期
                value = this.getDate(value, "（截止至：", 1);
                fundInfo.put(FundInfoTableMappingEnum.UNITS_DATE.getDbCode(),
                    DateUtil.parseDate(value.substring(0, value.length() - 1)));

                break;
            case BENCHMARK:
                rst = value.replace("该基金暂未披露业绩比较基准", "");
                break;
            case UNDERLYING:
                rst = value.replace("该基金无跟踪标的", "");
                break;
            default:
                rst = value;
                break;
        }
        fundInfo.put(mappingEnum.getDbCode(), rst);
        return rst;
    }

    /**
     * 从value中切割出年月日
     * @param value value是字符串
     * @param regxStr 根据regxStr对value进行切割得到数组
     * @param index 数组中的index取值
     * @return 行如1991年01月01日
     */
    private String getDate(String value, String regxStr, int index) {
        if (value.contains(regxStr)) {
            return value.split(regxStr)[index];
        } else {
            return DateUtil.DEFAULT_NULL_DATE;
        }
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