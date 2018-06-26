package com.hy.serviceImpl;

import com.hy.dao.IEquipmentPropertyDao;
import com.hy.entity.EquipmentProperty;
import com.hy.entity.EquipmentProperty1;
import com.hy.service.IEquipmentPropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class EquipmentPropertyServiceImpl implements IEquipmentPropertyService {
    @Resource
    IEquipmentPropertyDao equipmentPropertyDao;
    @Override
    public EquipmentProperty1 getEquipmentProperty1(EquipmentProperty1 equipmentProperty) {
        return equipmentPropertyDao.getEquipmentProperty1(equipmentProperty);
    }

    @Override
    public List<EquipmentProperty1> getList(Map<String, Object> map) {
        return equipmentPropertyDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return equipmentPropertyDao.getListCount(map);
    }

    @Override
    public Integer add(EquipmentProperty equipmentProperty) {
        return equipmentPropertyDao.add(equipmentProperty);
    }

    @Override
    public Integer update(EquipmentProperty equipmentProperty) {
        return equipmentPropertyDao.update(equipmentProperty);
    }

    @Override
    public Integer delete(EquipmentProperty equipmentProperty) {
        return equipmentPropertyDao.delete(equipmentProperty);
    }
}
