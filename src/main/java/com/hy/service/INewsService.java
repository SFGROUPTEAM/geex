package com.hy.service;

import com.hy.entity.News;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 */
public interface INewsService {

    public News getNews(News news);

    public List<News> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(News news);

    public Integer update(News news);

    public Integer delete(News news);
}
