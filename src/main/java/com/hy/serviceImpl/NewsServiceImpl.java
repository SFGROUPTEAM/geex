package com.hy.serviceImpl;

import com.hy.common.StringUtils;
import com.hy.dao.INewsDao;
import com.hy.entity.News;
import com.hy.service.INewsService;
import com.github.pagehelper.PageHelper;
import com.hy.service.INewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service
public class NewsServiceImpl implements INewsService {

    @Resource
    private INewsDao newsDao;
    @Override
    public News getNews(News news) {
        return newsDao.getNews(news);
    }

    @Override
    public List<News> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return newsDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return newsDao.getListCount(map);
    }

    @Override
    public Integer add(News news) {
        return newsDao.add(news);
    }

    @Override
    public Integer update(News news) {
        return newsDao.update(news);
    }

    @Override
    public Integer delete(News news) {
        return newsDao.delete(news);
    }
}
