package com.hy.dao;
import com.hy.entity.EquipmentLog;
import com.hy.entity.EquipmentLog1;

import java.util.List;
import java.util.Map;


/**
 * 道具日志DAO
 * */
public interface IEquipmentLogDao {

    public EquipmentLog getEquipmentLog(EquipmentLog log);

    public List<EquipmentLog1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentLog log);
}
