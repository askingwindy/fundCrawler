package base.contants;

/**
 * 处理代理类型
 * @author ruiying.hry
 * @version $Id: PageTypeEnum.java, v 0.1 2017-11-15 下午7:37 ruiying.hry Exp $$
 */
public enum ProxyTypeEnum {

    FUND_INFO("FUND_INFO", "单个基金页面的详情"),

    FUND_NAV("FUND_NAV", "单个基金的净值"),

    FUND_ALL("FUND_ALL", "所有基金信息");

    /** 配置场景code */
    private String code;

    /** 配置场景description */
    private String description;

    ProxyTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     *
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property <tt>description</tt>.
     *
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for property <tt>description</tt>.
     *
     * @param description value to be assigned to property description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}