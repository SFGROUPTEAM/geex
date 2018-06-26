package com.hy.entity;

public class PlatFormRole1 extends PlatFormRole {

    private Boolean  LAY_CHECKED;

    public Boolean getLAY_CHECKED() {
        return LAY_CHECKED;
    }

    public void setLAY_CHECKED(String LAY_CHECKED) {
        this.LAY_CHECKED = LAY_CHECKED.equals("1");
    }
}
