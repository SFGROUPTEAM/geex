package com.hy.serviceImpl.platform;

import com.hy.common.StringUtils;
import com.hy.dao.platform.IPlatFormUserDao;
import com.hy.entity.PlatFormUser;
import com.hy.service.platform.IPlatFormUserService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Lijianguo on 2018/4/24.
 * 平台用户Service实现类
 */
@Service
public class PlatFormUserServiceImpl implements IPlatFormUserService {
    @Resource
    private IPlatFormUserDao platFormUserDao;

    public PlatFormUser getPlatFormUser(PlatFormUser platFormUser){
        return  platFormUserDao.getPlatFormUser(platFormUser);
    }

    @Override
    public List<PlatFormUser> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return platFormUserDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return platFormUserDao.getListCount(map);
    }

    @Override
    public Integer add(PlatFormUser platFormUser) {
        return platFormUserDao.add(platFormUser);
    }

    @Override
    public Integer update(PlatFormUser platFormUser) {
        return platFormUserDao.update(platFormUser);
    }

    @Override
    public Integer delete(PlatFormUser platFormUser) {
        return platFormUserDao.delete(platFormUser);
    }

    @Override
    public Integer updatePwd(PlatFormUser platFormUser) {
        return platFormUserDao.updatePwd(platFormUser);
    }


}
