package com.victor.notary.model;

public class SecondPoint {
    private Integer id;

    private Double money;

    private Double credit;

    private Integer successnum;

    private Integer failnum;

    private Integer haderror;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getSuccessnum() {
        return successnum;
    }

    public void setSuccessnum(Integer successnum) {
        this.successnum = successnum;
    }

    public Integer getFailnum() {
        return failnum;
    }

    public void setFailnum(Integer failnum) {
        this.failnum = failnum;
    }

    public Integer getHaderror() {
        return haderror;
    }

    public void setHaderror(Integer haderror) {
        this.haderror = haderror;
    }
}