package com.hy.dao;

import com.hy.entity.News;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 */
@Repository
public interface INewsDao {
    public News getNews(News news);

    public List<News> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(News news);

    public Integer update(News news);

    public Integer delete(News news);
}
