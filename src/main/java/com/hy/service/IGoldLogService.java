package com.hy.service;


import com.hy.entity.GoldLog;

import java.util.List;
import java.util.Map;

public interface IGoldLogService {

    public GoldLog getGoldLog(GoldLog log);

    public List<GoldLog> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(GoldLog log);

}
