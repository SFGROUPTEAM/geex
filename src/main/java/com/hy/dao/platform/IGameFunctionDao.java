package com.hy.dao.platform;

import com.hy.entity.GameFunction;
import com.hy.entity.GameFunction1;

import java.util.List;
import java.util.Map;

public interface IGameFunctionDao {
    public GameFunction1 getGameFunctionByGameNo(String gameno);

    public Integer add(GameFunction gameFunction);

    public List<GameFunction1> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);


}
