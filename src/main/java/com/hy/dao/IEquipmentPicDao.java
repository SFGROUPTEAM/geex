package com.hy.dao;

import com.hy.entity.EquipmentPic;
import com.hy.entity.EquipmentPic1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 装备图片管理Dao
 */
@Repository
public interface IEquipmentPicDao {

    public EquipmentPic1 getEquipmentPic1(EquipmentPic equipmentPic);

    public List<EquipmentPic1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(EquipmentPic equipmentPic);

    public Integer update(EquipmentPic equipmentPic);

    public Integer delete(EquipmentPic equipmentPic);
}
