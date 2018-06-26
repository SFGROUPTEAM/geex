package com.hy.service;

import com.hy.entity.GameCategory;
import com.hy.entity.GameCategory1;

import java.util.List;
import java.util.Map;

/**
 * 游戏分类
 */
public interface IGameCategoryService {
    public GameCategory getGameCategory(GameCategory gameCategory);

    public List<GameCategory1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(GameCategory gameCategory);

    public Integer update(GameCategory gameCategory);

    public Integer delete(GameCategory gameCategory);
}
