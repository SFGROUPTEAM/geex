package com.hy.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.hy.common.StringUtils;
import com.hy.dao.IGamePicDao;
import com.hy.entity.GamePic;
import com.hy.entity.GamePic1;
import com.hy.service.IGamePicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class GamePicServiceImpl implements IGamePicService {
    @Resource
    IGamePicDao gamePicDao;
    @Override
    public GamePic1 getGamePic1(GamePic1 gamePic) {
        return gamePicDao.getGamePic1(gamePic);
    }

    @Override
    public List<GamePic1> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return gamePicDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return gamePicDao.getListCount(map);
    }

    @Override
    public Integer add(GamePic gamePic) {
        return gamePicDao.add(gamePic);
    }

    @Override
    public Integer update(GamePic gamePic) {
        return gamePicDao.update(gamePic);
    }

    @Override
    public Integer delete(GamePic gamePic) {
        return gamePicDao.delete(gamePic);
    }
}
