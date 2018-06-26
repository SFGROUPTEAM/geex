package com.hy.entity;

public class PlayerEquipment {
    private String playerid;

    private String equipmentid;

    private String equipmentname;

    private Integer quantity;

    private String createtime;

    private String updatetime;

    private Integer frozenquantiry;

    private Integer selledquantity;

    private String lastselledtime;

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid == null ? null : playerid.trim();
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid == null ? null : equipmentid.trim();
    }

    public String getEquipmentname() {
        return equipmentname;
    }

    public void setEquipmentname(String equipmentname) {
        this.equipmentname = equipmentname == null ? null : equipmentname.trim();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public Integer getFrozenquantiry() {
        return frozenquantiry;
    }

    public void setFrozenquantiry(Integer frozenquantiry) {
        this.frozenquantiry = frozenquantiry;
    }

    public Integer getSelledquantity() {
        return selledquantity;
    }

    public void setSelledquantity(Integer selledquantity) {
        this.selledquantity = selledquantity;
    }

    public String getLastselledtime() {
        return lastselledtime;
    }

    public void setLastselledtime(String lastselledtime) {
        this.lastselledtime = lastselledtime == null ? null : lastselledtime.trim();
    }
}
