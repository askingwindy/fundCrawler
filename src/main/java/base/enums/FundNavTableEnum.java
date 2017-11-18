package base.enums;

/**
 * 基金净值的数据库列名
 * @author ruiying.hry
 * @version $Id: FundNavTableEnum.java, v 0.1 2017-11-17 下午7:40 ruiying.hry Exp $$
 */
public enum FundNavTableEnum {

    FUND_CODE("fund_code", "基金代码"),

    RECORD_DATE("record_date", "净值日期"),

    NAV("nav", "单位净值"),

    ADD_NAV("add_nav", "累计净值"),

    NAV_CHG_RATE("nav_chg_rate", "日增长率(没有百分比)"),

    BUY_STATE("buy_state", "申购状态"),

    SELL_STATE("sell_state", "赎回状态"),

    DIV("div", "分红送配"),

    PROFIT_PER_UNITS("profit_per_units", "每万份收益"),

    PROFIT_RATE("profit_rate", "7日年化收益率(没有百分比)"),

    ;

    /**
     * 数据库的列名
     */
    private String dbCode;

    /**
     * 业务含义
     */
    private String descp;

    FundNavTableEnum(String dbCode, String descp) {
        this.dbCode = dbCode;
        this.descp = descp;
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
}
