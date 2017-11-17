package base.enums;

import util.StringUtils;

/**
 * 存储db中基金与名字的对应信息
 *
 * @author ruiying.hry
 * @version $Id: FundNavTableEnum.java, v 0.1 2017-11-16 下午1:56 ruiying.hry Exp $$
 */
public enum FundInfoTableMappingEnum {

    FUND_CODE("fund_code", "基金代码"),

    FUND_ALL_NAME("fund_name", "基金全称"),

    FUND_ABBR_NAME("fund_abbr_name", "基金简称"),

    FUND_TYPE("fund_type", "基金类型"),

    ISSUE_DATE("issue_date", "发行日期"),

    ESTABLISH_DATE("establish_date", "成立日期/规模", "成立日期"),

    ESTABLISH_SCALE("establish_scale", "成立日期/规模", "成立时的规模"),

    ASSET_VALUE("asset_value", "资产规模"),

    ASSET_VALUE_DATE("asset_value_date", "资产规模", "资产规模统计的截止日期"),

    UNITS("units", "份额规模"),

    UNITS_DATE("units_date", "份额规模", "份额规模统计的截止日期"),

    FUND_MANAGER("fund_manager", "基金管理人"),

    FUND_TRUSTER("fund_trustee", "基金托管人"),

    FUNDER("funder", "基金经理人"),

    TOTAL_DIV("total_div", "成立来分红"),

    MGT_FEE("mgt_fee", "管理费率"),

    TRUST_FEE("trust_fee", "托管费率"),

    SALE_FEE("sale_fee", "销售服务费率"),

    BUY_FEE("buy_fee", "最高认购费率"),

    BUY_FEE_2("buy_fee2", "最高申购费率"),

    BENCHMARK("benchmark", "业绩比较基准"),

    UNDERLYING("underlying", "跟踪标的"),

    ;

    /**数据库的列名*/
    private String dbCode;

    /**html爬到的页面切割key值*/
    private String chName;

    /**业务含义*/
    private String descp;

    FundInfoTableMappingEnum(String dbCode, String chName) {
        this.dbCode = dbCode;
        this.chName = chName;
    }

    FundInfoTableMappingEnum(String dbCode, String chName, String descp) {
        this.dbCode = dbCode;
        this.chName = chName;
        this.descp = descp;
    }

    /**
     * 根据中文映射名字,拿到数据库code
     * @param chName
     * @return
     */
    public static FundInfoTableMappingEnum getEnumByChName(String chName) {

        for (FundInfoTableMappingEnum fund : FundInfoTableMappingEnum.values()) {
            if (StringUtils.equals(fund.getChName(), chName)) {
                return fund;
            }
        }
        return null;
    }

    /**
     * Getter method for property   dbCode.
     *
     * @return property value of dbCode
     */
    public String getDbCode() {
        return dbCode;
    }

    /**
     * Setter method for property   dbCode .
     *
     * @param dbCode value to be assigned to property dbCode
     */
    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    /**
     * Getter method for property   chName.
     *
     * @return property value of chName
     */
    public String getChName() {
        return chName;
    }

    /**
     * Setter method for property   chName .
     *
     * @param chName value to be assigned to property chName
     */
    public void setChName(String chName) {
        this.chName = chName;
    }
}