package com.hy.service.api;

import com.hy.entity.PlayerLoginLength;

import java.util.List;
import java.util.Map;


public interface IPlayerLoginLengthService {
    public Integer add(PlayerLoginLength playerLoginLength);

    PlayerLoginLength getPlayerLoginLengthByGameNoAndVoucherNo(Map<String, Object> voucherNoMap);
}
