package com.hy.serviceImpl.api;

import com.hy.dao.api.IApiPlayerGoldDao;
import com.hy.service.api.IApiPlayerGoldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
@Service
public class ApiPlayerGoldServiceImpl implements IApiPlayerGoldService {
    @Resource
    IApiPlayerGoldDao playerGoldDao;
    @Override
    public void calculateGold(Map<String, Object> map) {
        playerGoldDao.calculateGold(map);
    }
}
