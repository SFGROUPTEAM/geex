package com.hy.entity;

/**
 * 用户VIT币资产表
 */
public class ExAsset {
    private String id;
    private String name;
    private String userid;
    private String goldcnt;
    private String goldfreezecnt;
    private String remark;
    private String address;
    private String state;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoldcnt() {
        return goldcnt;
    }

    public void setGoldcnt(String goldcnt) {
        this.goldcnt = goldcnt;
    }


    public String getGoldfreezecnt() {
        return goldfreezecnt;
    }

    public void setGoldfreezecnt(String goldfreezecnt) {
        this.goldfreezecnt = goldfreezecnt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
