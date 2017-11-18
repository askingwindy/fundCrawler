package base.enums;

import util.StringUtils;

/**
 * 基金经理管理历史数据
 *
 * @author sukki.qin
 * @version $Id: FunderExperienceInfoTableMappingEnum.java, v 0.1 2017-11-18 下午5:56 sukki.qin Exp $$
 */
public enum FunderExperienceInfoTableMappingEnum {

    FUNDER_CODE("funder_code","基金经理代码"),

    FUNDER_NAME("fund_name", "基金经理名字"),

    FUND_CODE("fund_code", "基金代码"),

    INCUMBENT_PERIOD("incumbent period","任职时间"),

    APPOINTMENT_DATE("appointment_date", "任期起始时间"),

    DIMISSION_DATE("dimission_date", "任期结束时间"),

    ONGOING("ongoing", "是否还在任期中"),

    TOTAL_RETURN("total_return","任职回报"),

    CREATE_TIME("create_time",  "数据创建时间"),

    UPDATE_TIME("update_time", "最后更新时间");

    /**数据库的列名*/
    private String dbCode;

    /**html爬到的页面切割key值*/
    private String chName;

    /**业务含义*/
    private String descp;

    FunderExperienceInfoTableMappingEnum(String dbCode, String chName) {
        this.dbCode = dbCode;
        this.chName = chName;
    }

    FunderExperienceInfoTableMappingEnum(String dbCode, String chName, String descp) {
        this.dbCode = dbCode;
        this.chName = chName;
        this.descp = descp;
    }

    /**
     * 根据中文映射名字,拿到数据库code
     * @param chName
     * @return
     */
    public static FunderExperienceInfoTableMappingEnum getEnumByChName(String chName) {

        for (FunderExperienceInfoTableMappingEnum fund : FunderExperienceInfoTableMappingEnum.values()) {
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