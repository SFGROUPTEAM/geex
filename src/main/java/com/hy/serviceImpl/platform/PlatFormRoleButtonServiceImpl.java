package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormRoleButtonDao;
import com.hy.entity.PlatFormRoleButton;
import com.hy.service.platform.IPlatFormRoleButtonService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormRoleButtonDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PlatFormRoleButtonServiceImpl implements IPlatFormRoleButtonService {
    @Resource
    private IPlatFormRoleButtonDao platFormRoleButtonDao;
    @Override
    public PlatFormRoleButton getPlatFormRoleButton(PlatFormRoleButton platFormRoleButton) {
        return platFormRoleButtonDao.getPlatFormRoleButton(platFormRoleButton);
    }

    @Override
    public List<PlatFormRoleButton> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormRoleButtonDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormRoleButtonDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormRoleButton platFormRoleButton) {
        return platFormRoleButtonDao.add(platFormRoleButton);
    }

    @Override
    public Integer update(PlatFormRoleButton platFormRoleButton) {
        return platFormRoleButtonDao.update(platFormRoleButton);
    }

    @Override
    public Integer delete(PlatFormRoleButton platFormRoleButton) {
        return platFormRoleButtonDao.delete(platFormRoleButton);
    }
}
