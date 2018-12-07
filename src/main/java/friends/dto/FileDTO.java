/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package friends.dto;

import util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ruiying.hry
 * @version $Id: FileDTO.java, v 0.1 2018年12月07日 7:21 PM ruiying.hry Exp $
 */
public class FileDTO implements Serializable {

    private static final long serialVersionUID = 1526143300992218509L;

    /***
     * 所属银行
     */
    private String            ownedBank;

    /**
     * 客户账号
     */
    private String            customerAccount;

    /**
     * 客户名称
     */
    private String            customerName;

    /**
     * 交易日期
     */
    private Date              tradeDate;

    /**
     * 交易日期-字符串
     */
    private String            tradeDateStr;

    /**
     * 收入金额
     */
    private BigDecimal        inMoney;

    /**
     * 支出金额
     */
    private BigDecimal        outMoney;

    /**
     * 余额
     */
    private BigDecimal        balanceMoney;

    /**
     * 对方账户
     */
    private String            tradeToAccount;

    /**
     * 对方名称
     */
    private String            tradeToName;

    /**
     * 摘要
     */
    private String            memo;

    /**
     * Getter method for property   ownedBank.
     *
     * @return property value of ownedBank
     */
    public String getOwnedBank() {
        return ownedBank;
    }

    /**
     * Setter method for property   ownedBank .
     *
     * @param ownedBank  value to be assigned to property ownedBank
     */
    public void setOwnedBank(String ownedBank) {
        this.ownedBank = ownedBank;
    }

    /**
     * Getter method for property   customerAccount.
     *
     * @return property value of customerAccount
     */
    public String getCustomerAccount() {
        return customerAccount;
    }

    /**
     * Setter method for property   customerAccount .
     *
     * @param customerAccount  value to be assigned to property customerAccount
     */
    public void setCustomerAccount(String customerAccount) {
        if (customerAccount.startsWith("A")) {
            customerAccount = customerAccount.substring(1);
        }
        this.customerAccount = customerAccount;
    }

    /**
     * Getter method for property   customerName.
     *
     * @return property value of customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Setter method for property   customerName .
     *
     * @param customerName  value to be assigned to property customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Getter method for property   tradeDate.
     *
     * @return property value of tradeDate
     */
    public Date getTradeDate() {
        return tradeDate;
    }

    /**
     * Setter method for property   tradeDate .
     *
     * @param tradeDate  value to be assigned to property tradeDate
     */
    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
        this.tradeDateStr = DateUtil.format(tradeDate, "yyyy-MM-dd");

    }

    /**
     * Getter method for property   tradeDateStr.
     *
     * @return property value of tradeDateStr
     */
    public String getTradeDateStr() {
        return tradeDateStr;
    }

    /**
     * Setter method for property   tradeDateStr .
     *
     * @param tradeDateStr  value to be assigned to property tradeDateStr
     */
    public void setTradeDateStr(String tradeDateStr) {
        this.tradeDateStr = tradeDateStr;
    }

    /**
     * Getter method for property   outMoney.
     *
     * @return property value of outMoney
     */
    public BigDecimal getOutMoney() {
        return outMoney;
    }

    /**
     * Setter method for property   outMoney .
     *
     * @param outMoney  value to be assigned to property outMoney
     */
    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney;
    }

    /**
     * Getter method for property   inMoney.
     *
     * @return property value of inMoney
     */
    public BigDecimal getInMoney() {
        return inMoney;
    }

    /**
     * Setter method for property   inMoney .
     *
     * @param inMoney  value to be assigned to property inMoney
     */
    public void setInMoney(BigDecimal inMoney) {
        this.inMoney = inMoney;
    }

    /**
     * Getter method for property   balanceMoney.
     *
     * @return property value of balanceMoney
     */
    public BigDecimal getBalanceMoney() {
        return balanceMoney;
    }

    /**
     * Setter method for property   balanceMoney .
     *
     * @param balanceMoney  value to be assigned to property balanceMoney
     */
    public void setBalanceMoney(BigDecimal balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    /**
     * Getter method for property   tradeToAccount.
     *
     * @return property value of tradeToAccount
     */
    public String getTradeToAccount() {
        return tradeToAccount;
    }

    /**
     * Setter method for property   tradeToAccount .
     *
     * @param tradeToAccount  value to be assigned to property tradeToAccount
     */
    public void setTradeToAccount(String tradeToAccount) {
        if (tradeToAccount.startsWith("A")) {
            tradeToAccount = tradeToAccount.substring(1);
        }
        this.tradeToAccount = tradeToAccount;
    }

    /**
     * Getter method for property   tradeToName.
     *
     * @return property value of tradeToName
     */
    public String getTradeToName() {
        return tradeToName;
    }

    /**
     * Setter method for property   tradeToName .
     *
     * @param tradeToName  value to be assigned to property tradeToName
     */
    public void setTradeToName(String tradeToName) {
        this.tradeToName = tradeToName;
    }

    /**
     * Getter method for property   memo.
     *
     * @return property value of memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Setter method for property   memo .
     *
     * @param memo  value to be assigned to property memo
     */
    public void setMemo(String memo) {
        memo = memo.replace("\\\t", "").replace("\t", "").replace("  ", "");
        memo = memo.trim();
        this.memo = memo;
    }
}