package com.hy.dao;

import com.hy.entity.GoldLog;

import java.util.List;
import java.util.Map;

/**
 * 金币日志Dao
 */
public interface IGoldLogDao {

    public GoldLog getGoldLog(GoldLog log);

    public List<GoldLog> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(GoldLog log);
}
