package com.hy.service;

import com.hy.entity.EquipmentPic;
import com.hy.entity.EquipmentPic1;

import java.util.List;
import java.util.Map;

/**
 * 装备图片管理Service
 */
public interface IEquipmentPicService {
    public EquipmentPic1 getEquipmentPic1(EquipmentPic equipmentPic);

    public List<EquipmentPic1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentPic equipmentPic);

    public Integer update(EquipmentPic equipmentPic);

    public Integer delete(EquipmentPic equipmentPic);
}
