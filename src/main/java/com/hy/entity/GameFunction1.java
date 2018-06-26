package com.hy.entity;

public class GameFunction1 extends GameFunction {
    private String gamestatus;
    private String gameno;
    private String gamename;
    private String gamestate;

    public String getGamestate() {
        return gamestate;
    }

    public void setGamestate(String gamestate) {
        this.gamestate = gamestate;
    }

    public String getGamestatus() {
        return gamestatus;
    }

    public void setGamestatus(String gamestatus) {
        this.gamestatus = gamestatus;
    }

    @Override
    public String getGameno() {
        return gameno;
    }

    @Override
    public void setGameno(String gameno) {
        this.gameno = gameno;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }
}
