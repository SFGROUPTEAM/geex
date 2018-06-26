package com.hy.serviceImpl.api;

import com.hy.dao.api.IPlayerLoginLengthDao;
import com.hy.entity.PlayerLoginLength;
import com.hy.service.api.IPlayerLoginLengthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PlayerLoginLengthServiceImpl implements IPlayerLoginLengthService {
    @Resource
    IPlayerLoginLengthDao playerLoginLengthDao;
    @Override
    public Integer add(PlayerLoginLength playerLoginLength) {
        return playerLoginLengthDao.add(playerLoginLength);
    }

    @Override
    public PlayerLoginLength getPlayerLoginLengthByGameNoAndVoucherNo(Map<String, Object> voucherNoMap) {
        return playerLoginLengthDao.getPlayerLoginLengthByGameNoAndVoucherNo(voucherNoMap);
    }

}
