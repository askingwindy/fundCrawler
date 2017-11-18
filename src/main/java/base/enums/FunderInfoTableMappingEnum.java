package base.enums;

import util.StringUtils;

/**
 * 基金经理基本信息数据
 *
 * @author sukki.qin
 * @version $Id: FunderInfoTableMappingEnum.java, v 0.1 2017-11-18 下午5:56 sukki.qin Exp $$
 */
public enum FunderInfoTableMappingEnum {

    FUNDER_CODE("funder_code","基金经理代码"),

    FUNDER_NAME("funder_name", "基金经理名字"),

    INAUGURATION_DATE("inauguration_date","任职起始日期"),

    INCUMBENT_COMPANY("incumbent_company", "现任基金公司"),

    ASSETS_SCALE("assets_scale", "现任基金资产总规模"),

    BEST_RETURN("best_return", "任职期间最佳基金回报"),

    DESCRIPTION("description","基金经理简介"),

    CREATE_TIME("create_time",  "数据创建时间"),

    UPDATE_TIME("update_time", "最后更新时间");

    /**数据库的列名*/
    private String dbCode;

    /**html爬到的页面切割key值*/
    private String chName;

    /**业务含义*/
    private String descp;

    FunderInfoTableMappingEnum(String dbCode, String chName) {
        this.dbCode = dbCode;
        this.chName = chName;
    }

    FunderInfoTableMappingEnum(String dbCode, String chName, String descp) {
        this.dbCode = dbCode;
        this.chName = chName;
        this.descp = descp;
    }

    /**
     * 根据中文映射名字,拿到数据库code
     * @param chName
     * @return
     */
    public static FunderInfoTableMappingEnum getEnumByChName(String chName) {

        for (FunderInfoTableMappingEnum fund : FunderInfoTableMappingEnum.values()) {
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