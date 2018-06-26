package com.hy.serviceImpl;

import com.hy.dao.IBaseConfigDao;
import com.hy.entity.BaseConfig;
import com.hy.service.IBaseConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BaseConfigServiceImpl implements IBaseConfigService{
@Resource
    IBaseConfigDao baseConfigDao;
    @Override
    public BaseConfig getBaseConfig() {
        return baseConfigDao.getBaseConfig();
    }
}
