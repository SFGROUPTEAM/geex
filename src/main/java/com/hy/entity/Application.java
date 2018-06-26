package com.hy.entity;

public class Application {
    private String id;

    private String applicationname;

    private Long sessionttl;

    private Integer isdefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getApplicationname() {
        return applicationname;
    }

    public void setApplicationname(String applicationname) {
        this.applicationname = applicationname == null ? null : applicationname.trim();
    }

    public Long getSessionttl() {
        return sessionttl;
    }

    public void setSessionttl(Long sessionttl) {
        this.sessionttl = sessionttl;
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}
