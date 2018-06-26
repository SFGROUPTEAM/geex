package com.hy.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.IEquipmentLogDao;
import com.hy.entity.EquipmentLog;
import com.hy.entity.EquipmentLog1;
import com.hy.service.IEquipmentLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentLogServiceImpl implements IEquipmentLogService{
    @Resource
    private IEquipmentLogDao equipmentLogDao;
    @Override
    public EquipmentLog getEquipmentLog(EquipmentLog log) {
        return equipmentLogDao.getEquipmentLog(log);
    }

    @Override
    public List<EquipmentLog1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return equipmentLogDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return equipmentLogDao.getListCount(map);
    }

    @Override
    public Integer add(EquipmentLog log) {
        return equipmentLogDao.add(log);
    }

}
