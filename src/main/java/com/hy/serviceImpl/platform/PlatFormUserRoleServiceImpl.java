package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormUserRoleDao;
import com.hy.entity.PlatFormUserRole;
import com.hy.service.platform.IPlatFormUserRoleService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormUserRoleDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PlatFormUserRoleServiceImpl implements IPlatFormUserRoleService {
    @Resource
    private IPlatFormUserRoleDao platFormUserRoleDao;
    @Override
    public PlatFormUserRole getPlatFormUserRole(PlatFormUserRole platFormUserRole) {
        return platFormUserRoleDao.getPlatFormUserRole(platFormUserRole);
    }

    @Override
    public List<PlatFormUserRole> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormUserRoleDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormUserRoleDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormUserRole platFormUserRole) {
        return platFormUserRoleDao.add(platFormUserRole);
    }

    @Override
    public Integer update(PlatFormUserRole platFormUserRole) {
        return platFormUserRoleDao.update(platFormUserRole);
    }

    @Override
    public Integer delete(PlatFormUserRole platFormUserRole) {
        return platFormUserRoleDao.delete(platFormUserRole);
    }
}
