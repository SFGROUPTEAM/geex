package com.hy.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.IEquipmentDao;
import com.hy.dao.IGoldLogDao;
import com.hy.entity.Equipment;
import com.hy.entity.Equipment1;
import com.hy.entity.GoldLog;
import com.hy.service.IEquipmentService;
import com.hy.service.IGoldLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GoldLogServiceImpl implements IGoldLogService {
    @Resource
    private IGoldLogDao goldLogDao;
    @Override
    public GoldLog getGoldLog(GoldLog log) {
        return goldLogDao.getGoldLog(log);
    }

    @Override
    public List<GoldLog> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return goldLogDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return goldLogDao.getListCount(map);
    }

    @Override
    public Integer add(GoldLog log) {
        return goldLogDao.add(log);
    }


}
