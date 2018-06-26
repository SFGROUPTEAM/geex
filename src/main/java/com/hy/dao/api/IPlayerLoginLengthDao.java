package com.hy.dao.api;

import com.hy.entity.PlayerLoginLength;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IPlayerLoginLengthDao {
    public Integer add(PlayerLoginLength  playerLoginLength);

    PlayerLoginLength getPlayerLoginLengthByGameNoAndVoucherNo(Map<String, Object> voucherNoMap);
}
