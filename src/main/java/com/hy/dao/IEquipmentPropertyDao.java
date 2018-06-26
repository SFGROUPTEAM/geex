package com.hy.dao;

import com.hy.entity.Equipment;
import com.hy.entity.EquipmentProperty;
import com.hy.entity.EquipmentProperty1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IEquipmentPropertyDao {
    public EquipmentProperty1 getEquipmentProperty1(EquipmentProperty1 equipmentProperty);

    public List<EquipmentProperty1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentProperty equipmentProperty);

    public Integer update(EquipmentProperty equipmentProperty);

    public Integer delete(EquipmentProperty equipmentProperty);
}
