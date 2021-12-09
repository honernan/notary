package com.victor.notary.model;

public class InteledgerNotary {
    private Integer id;

    private String ethchain1;

    private String useraddress1;

    private String userkeyfilejson1;

    private String money1;

    private String ethchain2;

    private String useraddress2;

    private String userkeyfilejson2;

    private String money2;

    private Integer fc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEthchain1() {
        return ethchain1;
    }

    public void setEthchain1(String ethchain1) {
        this.ethchain1 = ethchain1 == null ? null : ethchain1.trim();
    }

    public String getUseraddress1() {
        return useraddress1;
    }

    public void setUseraddress1(String useraddress1) {
        this.useraddress1 = useraddress1 == null ? null : useraddress1.trim();
    }

    public String getUserkeyfilejson1() {
        return userkeyfilejson1;
    }

    public void setUserkeyfilejson1(String userkeyfilejson1) {
        this.userkeyfilejson1 = userkeyfilejson1 == null ? null : userkeyfilejson1.trim();
    }

    public String getMoney1() {
        return money1;
    }

    public void setMoney1(String money1) {
        this.money1 = money1 == null ? null : money1.trim();
    }

    public String getEthchain2() {
        return ethchain2;
    }

    public void setEthchain2(String ethchain2) {
        this.ethchain2 = ethchain2 == null ? null : ethchain2.trim();
    }

    public String getUseraddress2() {
        return useraddress2;
    }

    public void setUseraddress2(String useraddress2) {
        this.useraddress2 = useraddress2 == null ? null : useraddress2.trim();
    }

    public String getUserkeyfilejson2() {
        return userkeyfilejson2;
    }

    public void setUserkeyfilejson2(String userkeyfilejson2) {
        this.userkeyfilejson2 = userkeyfilejson2 == null ? null : userkeyfilejson2.trim();
    }

    public String getMoney2() {
        return money2;
    }

    public void setMoney2(String money2) {
        this.money2 = money2 == null ? null : money2.trim();
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }
}