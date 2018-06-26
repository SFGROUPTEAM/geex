package com.hy.dao;

import com.hy.entity.GamePic;
import com.hy.entity.GamePic1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 游戏图片管理
 */
@Repository
public interface IGamePicDao {
    GamePic1 getGamePic1(GamePic1 gamePic);

    List<GamePic1> getList(Map<String, Object> map);

    Integer getListCount(Map<String, Object> map);

    Integer add(GamePic gamePic);

    Integer update(GamePic gamePic);

    Integer delete(GamePic gamePic);
}
