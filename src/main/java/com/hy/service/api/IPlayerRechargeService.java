package com.hy.service.api;

import com.hy.entity.PlayerRecharge;

import java.util.Map;



public interface IPlayerRechargeService {
    public Integer add(PlayerRecharge playerRecharge);

    PlayerRecharge getPlayerRechargeByGameNoAndOrderNo(Map<String, Object> orderNoMap);
}
