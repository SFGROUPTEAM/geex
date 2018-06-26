package com.hy.service;

import com.hy.entity.Equipment;
import com.hy.entity.Equipment1;

import java.util.List;
import java.util.Map;

public interface IEquipmentService {

    public Equipment1 getEquipment1(Equipment1 equipment);

    public List<Equipment1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    //获取首页装备展示列表
    public List<Equipment1> getAllEquipmentList(Map<String, Object> map);

    //获取首页装备展示数量
    public Integer getAllEquipmentCount(Map<String, Object> map);

    public List<Equipment> getSimpleEquipmentList(Map<String, Object> map);

    public Integer getSimpleEquipmentListCount(Map<String, Object> map);

    public Integer add(Equipment equipment);

    public Integer update(Equipment equipment);

    public Integer delete(Equipment equipment);

    /*//获取分类下装备列表
    public List<Equipment1> getClassEquipmentList(Map<String, Object> map);

    //获取分类下装备数量
    public Integer getClassEquipmentCount(Map<String, Object> map);*/
}
