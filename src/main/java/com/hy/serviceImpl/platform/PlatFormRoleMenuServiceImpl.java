package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormRoleMenuDao;
import com.hy.entity.PlatFormRoleMenu;
import com.hy.service.platform.IPlatFormRoleMenuService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormRoleMenuDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PlatFormRoleMenuServiceImpl implements IPlatFormRoleMenuService {
    @Resource
    private IPlatFormRoleMenuDao platFormRoleMenuDao;
    @Override
    public PlatFormRoleMenu getPlatFormRoleMenu(PlatFormRoleMenu platFormRoleMenu) {
        return platFormRoleMenuDao.getPlatFormRoleMenu(platFormRoleMenu);
    }

    @Override
    public List<PlatFormRoleMenu> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormRoleMenuDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormRoleMenuDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormRoleMenu platFormRoleMenu) {
        return platFormRoleMenuDao.add(platFormRoleMenu);
    }

    @Override
    public Integer update(PlatFormRoleMenu platFormRoleMenu) {
        return platFormRoleMenuDao.update(platFormRoleMenu);
    }

    @Override
    public Integer delete(PlatFormRoleMenu platFormRoleMenu) {
        return platFormRoleMenuDao.delete(platFormRoleMenu);
    }
}
