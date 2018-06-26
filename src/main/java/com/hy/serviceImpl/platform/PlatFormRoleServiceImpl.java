package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormRoleDao;
import com.hy.entity.PlatFormRole;
import com.hy.entity.PlatFormRole1;
import com.hy.service.platform.IPlatFormRoleService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormRoleDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PlatFormRoleServiceImpl implements IPlatFormRoleService{
    @Resource
    private IPlatFormRoleDao iPlatFormRoleDao;
    @Override
    public PlatFormRole getPlatFormRole(PlatFormRole platFormRole) {
        return iPlatFormRoleDao.getPlatFormRole(platFormRole);
    }

    @Override
    public List<PlatFormRole> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return iPlatFormRoleDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return iPlatFormRoleDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormRole platFormRole) {
        return iPlatFormRoleDao.add(platFormRole);
    }

    @Override
    public Integer update(PlatFormRole platFormRole) {
        return iPlatFormRoleDao.update(platFormRole);
    }

    @Override
    public Integer delete(PlatFormRole platFormRole) {
        return iPlatFormRoleDao.delete(platFormRole);
    }

    public List<PlatFormRole1> qryUserRoleList(Map<String, Object> map){
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return  iPlatFormRoleDao.qryUserRoleList(map);
    };
    @Override
    public Integer getListCount1(Map<String, Object> map) {
        return iPlatFormRoleDao.getListCount1(map);
    }

}
