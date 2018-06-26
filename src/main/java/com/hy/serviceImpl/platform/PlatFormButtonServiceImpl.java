package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormButtonDao;
import com.hy.entity.PlatFormButton;
import com.hy.entity.PlatFormButton1;
import com.hy.service.platform.IPlatFormButtonService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.platform.IPlatFormButtonDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class PlatFormButtonServiceImpl implements IPlatFormButtonService {
    @Resource
    private IPlatFormButtonDao platFormButtonDao;
    @Override
    public PlatFormButton getPlatFormButton(PlatFormButton platFormButton) {
        return platFormButtonDao.getPlatFormButton(platFormButton);
    }

    @Override
    public List<PlatFormButton> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormButtonDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormButtonDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormButton platFormButton) {
        return platFormButtonDao.add(platFormButton);
    }

    @Override
    public Integer update(PlatFormButton platFormButton) {
        return platFormButtonDao.update(platFormButton);
    }

    @Override
    public Integer delete(PlatFormButton platFormButton) {
        return platFormButtonDao.delete(platFormButton);
    }

    @Override
    public List<PlatFormButton1> getRoleButtonList(Map<String, Object> map){return platFormButtonDao.getRoleButtonList(map);}

}
