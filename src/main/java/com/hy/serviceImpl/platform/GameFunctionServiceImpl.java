package com.hy.serviceImpl.platform;

import com.hy.dao.platform.IGameFunctionDao;
import com.hy.entity.GameFunction;
import com.hy.entity.GameFunction1;
import com.hy.service.platform.IGameFunctionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class GameFunctionServiceImpl implements IGameFunctionService {
    @Resource
    IGameFunctionDao gameFunctionDao;
    @Override
    public GameFunction1 getGameFunctionByGameNo(String gameno) {
        return gameFunctionDao.getGameFunctionByGameNo(gameno);
    }
    @Override
    public Integer add(GameFunction gameFunction){
        return gameFunctionDao.add(gameFunction);
    }
    @Override
    public List<GameFunction1> getList(Map<String, Object> map){
        return gameFunctionDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return gameFunctionDao.getListCount(map);
    }

}
