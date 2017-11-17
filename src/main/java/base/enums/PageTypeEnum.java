/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package base.enums;

/**
 *
 * @author ruiying.hry
 * @version $Id: PageTypeEnum.java, v 0.1 2017-11-17 下午12:20 ruiying.hry Exp $$
 */
public enum PageTypeEnum {
    FUND_INFO("FUND_INFO", "单个基金页面的详情", "http://fund.eastmoney.com/f10/jbgk_"),

    FUND_NAV("FUND_NAV", "单个基金的净值"),

    FUND_MANAGER("FUND_MANAGER", "基金经理信息"),

    FUND_ALL("FUND_ALL", "所有基金信息");

    /** 配置场景code */
    private String code;

    /** 配置场景description */
    private String description;

    /**页面地址前缀*/
    private String htmPrefix;

    PageTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    PageTypeEnum(String code, String description, String htmPrefix) {
        this.code = code;
        this.description = description;
        this.htmPrefix = htmPrefix;
    }

    /**
     * Getter method for property   htmPrefix.
     *
     * @return property value of htmPrefix
     */
    public String getHtmPrefix() {
        return htmPrefix;
    }

    /**
     * Setter method for property   htmPrefix .
     *
     * @param htmPrefix value to be assigned to property htmPrefix
     */
    public void setHtmPrefix(String htmPrefix) {
        this.htmPrefix = htmPrefix;
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