package com.hy.serviceImpl.player;

import com.hy.common.StringUtils;
import com.hy.dao.player.IPlayerUserDao;
import com.hy.entity.ExAsset;
import com.hy.entity.ExUser;
import com.hy.entity.Player;
import com.hy.entity.Player1;
import com.hy.service.player.IPlayerUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import com.github.pagehelper.PageHelper;

@Service
public class PlayerUserServiceImpl implements IPlayerUserService {

    @Resource
    private IPlayerUserDao playerUserDao;

    @Override
    public Player1 getPlayerUser(Player player) {
            return playerUserDao.getPlayerUser(player);
    }

    @Override
    public List<Player> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return playerUserDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return playerUserDao.getListCount(map);
    }

    @Override
    public Integer add(Player player) {
        return playerUserDao.add(player);
    }

    @Override
    public Integer update(Player player) {
        return playerUserDao.update(player);
    }

    @Override
    public void login(Map<String, Object> map) {
        playerUserDao.login(map);
    }

    @Override
    public Integer exchange(Map<String, Object> map) {
        return playerUserDao.exchange(map);
    }

    @Override
    public ExUser getExUser(ExUser exUser) {
        return playerUserDao.getExUser(exUser);
    }

    @Override
    public Integer addExUser(ExUser exUser) {
        return playerUserDao.addExUser(exUser);
    }

    @Override
    public Integer addExAsset(ExAsset exAsset) {
        return playerUserDao.addExAsset(exAsset);
    }
}
