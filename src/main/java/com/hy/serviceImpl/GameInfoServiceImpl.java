package com.hy.serviceImpl;

import com.hy.common.StringUtils;
import com.hy.dao.IGameInfoDao;
import com.hy.entity.*;
import com.hy.service.IGameInfoService;
import com.github.pagehelper.PageHelper;
import com.hy.service.IGameInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GameInfoServiceImpl implements IGameInfoService {

    @Resource
    private IGameInfoDao gameInfoDao;

    @Override
    public GameInfo1 getGameInfo(GameInfo gameInfo) {
        return gameInfoDao.getGameInfo(gameInfo);
    }

    @Override
    public List<GameInfo2> getList(Map<String, Object> map) {
        return gameInfoDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return gameInfoDao.getListCount(map);
    }


    @Override
    public List<GameInfo2> getAllGameList(Map<String, Object> map) {
        int pagenum = 1;
        int pagesize = 999999;
        if (map.get("pagenum") != null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum = Integer.parseInt(map.get("pagenum").toString());
        if (map.get("pagesize") != null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize = Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum, pagesize);
        return gameInfoDao.getAllGameList(map);
    }

    @Override
    public Integer getAllGameCount(Map<String, Object> map) {
        return gameInfoDao.getAllGameCount(map);
    }


    @Override
    public List<GameInfo2> getclassGameList(Map<String, Object> map) {
        int pagenum = 1;
        int pagesize = 999999;
        if (map.get("pagenum") != null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum = Integer.parseInt(map.get("pagenum").toString());
        if (map.get("pagesize") != null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize = Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum, pagesize);
        return gameInfoDao.getclassGameList(map);
    }

    @Override
    public Integer getClassGameListCount(Map<String, Object> map) {
        return gameInfoDao.getClassGameListCount(map);
    }




    @Override
    public List<GameInfo1> getList1(Map<String, Object> map) {
        int pagenum = 1;
        int pagesize = 999999;
        if (map.get("pagenum") != null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum = Integer.parseInt(map.get("pagenum").toString());
        if (map.get("pagesize") != null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize = Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum, pagesize);
        return gameInfoDao.getList1(map);
    }

    @Override
    public Integer getListCount1(Map<String, Object> map) {
        return gameInfoDao.getListCount1(map);
    }

    @Override
    public List<Map<String,Object>> getNewList(Map<String, Object> map) {
        return gameInfoDao.getNewList(map);
    }

    @Override
    public Integer add(GameInfo gameInfo) {
        return gameInfoDao.add(gameInfo);
    }

    @Override
    public Integer update(GameInfo gameInfo) {
        return gameInfoDao.update(gameInfo);
    }


    public List<GameFunctionExport>  getFunctionList(Map<String, Object> map){
        return  gameInfoDao.getFunctionList(map);
    }




    @Override
    public Integer delete(GameInfo gameInfo) {
        return gameInfoDao.delete(gameInfo);
    }


}
