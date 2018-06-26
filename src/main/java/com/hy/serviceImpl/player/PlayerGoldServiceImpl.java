package com.hy.serviceImpl.player;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.player.IPlayerEquipmentDao;
import com.hy.dao.player.IPlayerGoldDao;
import com.hy.entity.PlayerEquipment1;
import com.hy.entity.PlayerGold;
import com.hy.service.player.IPlayerGoldService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PlayerGoldServiceImpl implements IPlayerGoldService {

    @Resource
    private IPlayerGoldDao playerGoldDao;

    @Override
    public PlayerGold getPlayerGold(PlayerGold playerGold) {
            return playerGoldDao.getPlayerGold(playerGold);
    }

    @Override
    public List<PlayerGold> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return playerGoldDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return playerGoldDao.getListCount(map);
    }

}
