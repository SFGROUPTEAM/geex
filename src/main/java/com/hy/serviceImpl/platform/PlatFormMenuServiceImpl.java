package com.hy.serviceImpl.platform;


import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormMenuDao;
import com.hy.entity.PlatFormMenu;
import com.hy.entity.PlatFormMenu1;
import com.hy.service.platform.IPlatFormMenuService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormMenuDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PlatFormMenuServiceImpl implements IPlatFormMenuService {

    @Resource
    private IPlatFormMenuDao platFormMenuDao;
    @Override
    public PlatFormMenu getPlatFormMenu(PlatFormMenu platFormMenu) {
        return platFormMenuDao.getPlatFormMenu(platFormMenu);
    }

    @Override
    public List<PlatFormMenu> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormMenuDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormMenuDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormMenu platFormMenu) {
        return platFormMenuDao.add(platFormMenu);
    }

    @Override
    public Integer update(PlatFormMenu platFormMenu) {
        return platFormMenuDao.update(platFormMenu);
    }

    @Override
    public Integer delete(PlatFormMenu platFormMenu) {
        return platFormMenuDao.delete(platFormMenu);
    }

    @Override
    public List<PlatFormMenu> getUserList(Map<String, Object> map){ return  platFormMenuDao.getUserList(map);}

    @Override
    public List<PlatFormMenu1> getRoleMenuList(Map<String, Object> map){return  platFormMenuDao.getRoleMenuList(map);}

}
