package com.hy.serviceImpl.player;

import com.hy.common.StringUtils;
import com.hy.dao.player.IPlayerEquipmentDao;
import com.hy.entity.PlayerEquipment;
import com.hy.entity.PlayerEquipment1;
import com.hy.service.player.IPlayerEquipmentService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.player.IPlayerEquipmentDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PlayerEquipmentServiceImpl implements IPlayerEquipmentService {

    @Resource
    private IPlayerEquipmentDao playerEquipmentDao;

    @Override
    public PlayerEquipment getPlayerEquipment(PlayerEquipment playerEquipment) {
            return playerEquipmentDao.getPlayerEquipment(playerEquipment);
    }

    @Override
    public List<PlayerEquipment1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return playerEquipmentDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return playerEquipmentDao.getListCount(map);
    }

    @Override
    public Integer add(PlayerEquipment playerEquipment) {
        return playerEquipmentDao.add(playerEquipment);
    }

    @Override
    public Integer update(PlayerEquipment playerEquipment) {
        return playerEquipmentDao.update(playerEquipment);
    }
}
