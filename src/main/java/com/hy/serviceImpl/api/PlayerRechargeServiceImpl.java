package com.hy.serviceImpl.api;

import com.hy.dao.api.IPlayerRechargeDao;
import com.hy.entity.PlayerRecharge;
import com.hy.service.api.IPlayerRechargeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PlayerRechargeServiceImpl implements IPlayerRechargeService {
    @Resource
    IPlayerRechargeDao playerRechargeDao;
    @Override
    public Integer add(PlayerRecharge playerRecharge) {
        return playerRechargeDao.add(playerRecharge);
    }

    @Override
    public PlayerRecharge getPlayerRechargeByGameNoAndOrderNo(Map<String, Object> orderNoMap) {
        return playerRechargeDao.getPlayerRechargeByGameNoAndOrderNo(orderNoMap);
    }
}
