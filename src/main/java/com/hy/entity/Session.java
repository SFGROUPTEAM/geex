package com.hy.entity;

public class Session {
    private String id;

    private String applicationid;

    private String userid;

    private String visittime;

    private String visitip;

    private String sessiondata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getApplicationid() {
        return applicationid;
    }

    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid == null ? null : applicationid.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getVisittime() {
        return visittime;
    }

    public void setVisittime(String visittime) {
        this.visittime = visittime == null ? null : visittime.trim();
    }

    public String getVisitip() {
        return visitip;
    }

    public void setVisitip(String visitip) {
        this.visitip = visitip == null ? null : visitip.trim();
    }

    public String getSessiondata() {
        return sessiondata;
    }

    public void setSessiondata(String sessiondata) {
        this.sessiondata = sessiondata == null ? null : sessiondata.trim();
    }
}
