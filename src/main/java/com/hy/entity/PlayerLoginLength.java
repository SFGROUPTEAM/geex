package com.hy.entity;

public class PlayerLoginLength {
    private String id;
    private String playerid;
    private String gameno;
    private Integer loginlength;
    private String voucherno;

    public String getVoucherno() {
        return voucherno;
    }

    public void setVoucherno(String voucherno) {
        this.voucherno = voucherno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public String getGameno() {
        return gameno;
    }

    public void setGameno(String gameno) {
        this.gameno = gameno;
    }

    public Integer getLoginlength() {
        return loginlength;
    }

    public void setLoginlength(Integer loginlength) {
        this.loginlength = loginlength;
    }
}
