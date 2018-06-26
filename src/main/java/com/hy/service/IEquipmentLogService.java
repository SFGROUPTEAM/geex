package com.hy.service;

import com.hy.entity.EquipmentLog;
import com.hy.entity.EquipmentLog1;

import java.util.List;
import java.util.Map;

public interface IEquipmentLogService {

    public EquipmentLog getEquipmentLog(EquipmentLog log);

    public List<EquipmentLog1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentLog log);

}
