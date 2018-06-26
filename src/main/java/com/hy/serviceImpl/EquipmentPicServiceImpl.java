package com.hy.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.IEquipmentPicDao;
import com.hy.entity.EquipmentPic;
import com.hy.entity.EquipmentPic1;
import com.hy.service.IEquipmentPicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class EquipmentPicServiceImpl implements IEquipmentPicService{
    @Resource
    IEquipmentPicDao equipmentPicDao;
    @Override
    public EquipmentPic1 getEquipmentPic1(EquipmentPic equipmentPic) {
        return equipmentPicDao.getEquipmentPic1(equipmentPic);
    }

    @Override
    public List<EquipmentPic1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return equipmentPicDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return equipmentPicDao.getListCount(map);
    }

    @Override
    public Integer add(EquipmentPic equipmentPic) {
        return equipmentPicDao.add(equipmentPic);
    }

    @Override
    public Integer update(EquipmentPic equipmentPic) {
        return equipmentPicDao.update(equipmentPic);
    }

    @Override
    public Integer delete(EquipmentPic equipmentPic) {
        return equipmentPicDao.delete(equipmentPic);
    }
}
