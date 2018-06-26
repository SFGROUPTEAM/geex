package com.hy.service;

import com.hy.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 游戏信息
 */
public interface IGameInfoService {

        public GameInfo1 getGameInfo(GameInfo gameInfo);

        public List<GameInfo2> getList(Map<String, Object> map);

        public Integer getListCount(Map<String, Object> map);

        //获取首页游戏展示列表
        public List<GameInfo2> getAllGameList(Map<String, Object> map);

        //获取首页游戏数量
        public Integer getAllGameCount(Map<String, Object> map);

        //获取分类游戏下的所有游戏
        public List<GameInfo2> getclassGameList(Map<String, Object> map);

        //获取分类游戏下的所有游戏总量
        public Integer getClassGameListCount(Map<String, Object> map);

        //获取最新公告
        public List<Map<String,Object>> getNewList(Map<String, Object> map);

        public List<GameInfo1> getList1(Map<String, Object> map);

        public Integer getListCount1(Map<String, Object> map);

        public Integer add(GameInfo gameInfo);

        public Integer update(GameInfo gameInfo);

        public Integer delete(GameInfo gameInfo);

        /*public List<GameCategory> qryGameCateChildren(String id);*/

        /* //获取A商城首页的游戏展示列表
        public List<GameInfo2> getPlayerGameList1(String categoryid);

        //获取A商城首页的游戏分类列表
        public List<GameCategory> getPlayerGameClassList1(Map<String, Object> map);*/

        public List<GameFunctionExport>  getFunctionList(Map<String, Object> map);

}
