package com.victor.notary.model;

import java.util.Date;

public class TxInfoBetweenEth {
    private Integer id;

    private String from;

    private String to;

    private String sourceblockhash;

    private String targetblockhash;

    private String sourcetxhash;

    private String targettxhash;

    private String money;

    private Date starttime;

    private Date endtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to == null ? null : to.trim();
    }

    public String getSourceblockhash() {
        return sourceblockhash;
    }

    public void setSourceblockhash(String sourceblockhash) {
        this.sourceblockhash = sourceblockhash == null ? null : sourceblockhash.trim();
    }

    public String getTargetblockhash() {
        return targetblockhash;
    }

    public void setTargetblockhash(String targetblockhash) {
        this.targetblockhash = targetblockhash == null ? null : targetblockhash.trim();
    }

    public String getSourcetxhash() {
        return sourcetxhash;
    }

    public void setSourcetxhash(String sourcetxhash) {
        this.sourcetxhash = sourcetxhash == null ? null : sourcetxhash.trim();
    }

    public String getTargettxhash() {
        return targettxhash;
    }

    public void setTargettxhash(String targettxhash) {
        this.targettxhash = targettxhash == null ? null : targettxhash.trim();
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}