package com.hy.service;

import com.hy.entity.GamePic;
import com.hy.entity.GamePic1;


import java.util.List;
import java.util.Map;

/**
 * 游戏图片管理
 */

public interface IGamePicService {
    GamePic1 getGamePic1(GamePic1 gamePic);

    List<GamePic1> getList(Map<String, Object> map);

    Integer getListCount(Map<String, Object> map);

    Integer add(GamePic gamePic);

    Integer update(GamePic gamePic);

    Integer delete(GamePic gamePic);
}
