package com.hy.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;


public class GameFunctionExport {
    @Excel(name = "游戏名称", orderNum = "0")
    private String gamename;

    @Excel(name = "游戏编号", orderNum = "1")
    private String gameno;

    @Excel(name = "公钥", orderNum = "2")
    private String publickey;

    @Excel(name = "私钥", orderNum = "3")
    private String privatekey;

    public GameFunctionExport() {

    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getGameno() {
        return gameno;
    }

    public void setGameno(String gameno) {
        this.gameno = gameno;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }
}
