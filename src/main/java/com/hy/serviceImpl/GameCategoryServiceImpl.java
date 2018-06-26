package com.hy.serviceImpl;

import com.hy.common.StringUtils;
import com.hy.dao.IGameCategoryDao;
import com.hy.entity.GameCategory;
import com.hy.entity.GameCategory1;
import com.hy.service.IGameCategoryService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.IGameCategoryDao;
import com.hy.service.IGameCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class GameCategoryServiceImpl implements IGameCategoryService {
    @Resource
    private IGameCategoryDao gameCategoryDao;
    @Override
    public GameCategory getGameCategory(GameCategory gameCategory) {
        return gameCategoryDao.getGameCategory(gameCategory);
    }

    @Override
    public List<GameCategory1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return gameCategoryDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return gameCategoryDao.getListCount(map);
    }

    @Override
    public Integer add(GameCategory gameCategory) {
        return gameCategoryDao.add(gameCategory);
    }

    @Override
    public Integer update(GameCategory gameCategory) {
        return gameCategoryDao.update(gameCategory);
    }

    @Override
    public Integer delete(GameCategory gameCategory) {
        return gameCategoryDao.delete(gameCategory);
    }
}
