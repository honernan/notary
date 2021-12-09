package com.victor.notary.model;

public class EthUser1 {
    private Integer id;

    private String url;

    private String useraddress;

    private String userkeyfilejson;

    private Integer flag;

    private String money;

    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress == null ? null : useraddress.trim();
    }

    public String getUserkeyfilejson() {
        return userkeyfilejson;
    }

    public void setUserkeyfilejson(String userkeyfilejson) {
        this.userkeyfilejson = userkeyfilejson == null ? null : userkeyfilejson.trim();
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    } {
        this.money = money == null ? null : money.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

}