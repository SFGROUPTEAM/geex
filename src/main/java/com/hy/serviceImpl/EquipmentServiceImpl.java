package com.hy.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.IEquipmentDao;
import com.hy.entity.Equipment;
import com.hy.entity.Equipment1;
import com.hy.service.IEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class EquipmentServiceImpl implements IEquipmentService {
    @Resource
    private IEquipmentDao equipmentDao;
    @Override
    public Equipment1 getEquipment1(Equipment1 equipment) {
        return equipmentDao.getEquipment1(equipment);
    }

    @Override
    public List<Equipment1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return equipmentDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return equipmentDao.getListCount(map);
    }

    @Override
    public List<Equipment1> getAllEquipmentList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return equipmentDao.getAllEquipmentList(map);
    }

    @Override
    public Integer getAllEquipmentCount(Map<String, Object> map) {
        return equipmentDao.getAllEquipmentCount(map);
    }

    @Override
    public List<Equipment> getSimpleEquipmentList(Map<String, Object> map) {
        return equipmentDao.getSimpleEquipmentList(map);
    }

    @Override
    public Integer getSimpleEquipmentListCount(Map<String, Object> map) {
        return equipmentDao.getSimpleEquipmentListCount(map);
    }

    @Override
    public Integer add(Equipment equipment) {
        return equipmentDao.add(equipment);
    }

    @Override
    public Integer update(Equipment equipment) {
        return equipmentDao.update(equipment);
    }

    @Override
    public Integer delete(Equipment equipment) {
        return equipmentDao.delete(equipment);
    }
}
