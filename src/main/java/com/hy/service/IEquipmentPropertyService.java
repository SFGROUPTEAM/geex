package com.hy.service;

import com.hy.entity.EquipmentProperty;
import com.hy.entity.EquipmentProperty1;


import java.util.List;
import java.util.Map;

/**
 * 装备扩展表
 */

public interface IEquipmentPropertyService {
    public EquipmentProperty1 getEquipmentProperty1(EquipmentProperty1 equipmentProperty);

    public List<EquipmentProperty1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentProperty equipmentProperty);

    public Integer update(EquipmentProperty equipmentProperty);

    public Integer delete(EquipmentProperty equipmentProperty);
}
