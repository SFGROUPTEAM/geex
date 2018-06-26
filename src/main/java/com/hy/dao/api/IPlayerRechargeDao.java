package com.hy.dao.api;

import com.hy.entity.PlayerRecharge;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IPlayerRechargeDao {
    public Integer add(PlayerRecharge playerRecharge);

    PlayerRecharge getPlayerRechargeByGameNoAndOrderNo(Map<String, Object> orderNoMap);
}
