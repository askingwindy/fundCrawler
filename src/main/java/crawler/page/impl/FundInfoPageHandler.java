package crawler.page.impl;

import base.contants.FundTableNameContants;
import base.enums.FundInfoTableMappingEnum;
import com.alibaba.common.lang.StringUtil;
import crawler.page.PageHandler;
import manager.DatabaseManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DateUtil;
import util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫类的主体,利用开源java爬虫框架(Crawler4j)实现
 * @author ruiying.hry
 * @version $Id: FundInfoPageHandler.java, v 0.1 2017-11-15 下午6:01 ruiying.hry Exp $$
 */
public class FundInfoPageHandler implements PageHandler {
    /** 日志管理 */
    private static Logger   logger = LoggerFactory.getLogger(FundInfoPageHandler.class);

    private String          fundCode;

    private DatabaseManager databaseManager;

    public FundInfoPageHandler(String fundCode, DatabaseManager databaseManager) {
        this.fundCode = fundCode;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean handle(String htmltext) {

        LogUtil.info(logger, "page handle fund start,code=" + fundCode);

        if (StringUtil.isEmpty(fundCode)) {
            throw new RuntimeException("基金代码不存在,无法执行逻辑");
        }

        //0. 准备map,这个用于写入数据库中
        Map<String, Object> fundInfo = new HashMap<String, Object>();

        //1. 从html的选择器中拿到table显示的数据
        Document doc = Jsoup.parse(htmltext);

        Elements fundTableElement = doc.select("table.info").select(".w790");

        //2. 通过映射,找到对应的枚举
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
            LogUtil.info(logger, fundInfo.toString());

            databaseManager.insert(FundTableNameContants.FUND_INFO_TALBE_NAME, fundInfo);
        } catch (Exception e) {
            LogUtil.error(logger, e, "handle fund info failed, code=" + fundCode);
            return false;
        }

        LogUtil.info(logger, "handle fund success,code=" + fundCode);

        return true;
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
                fundInfo.put(FundInfoTableMappingEnum.ESTABLISH_SCALE.getDbCode(),
                    value.split("/")[1].trim());
                break;
            case ASSET_VALUE:
                rst = value.split("（截止至")[0].trim();

                //资产统计截止日期
                value = value.split("（截止至：")[1];
                fundInfo.put(FundInfoTableMappingEnum.ASSET_VALUE_DATE.getDbCode(),
                    DateUtil.parseDate(value.substring(0, value.length() - 1)));
                break;
            case UNITS:
                //2.2736亿份（截止至：2017年09月30日）
                rst = value.split("（")[0];

                //份额统计最后日期
                value = value.split("（截止至：")[1];
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
     * Setter method for property   fundCode .
     *
     * @param fundCode value to be assigned to property fundCode
     */
    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}