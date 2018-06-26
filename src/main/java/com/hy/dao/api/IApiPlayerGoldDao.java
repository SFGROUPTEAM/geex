package com.hy.dao.api;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IApiPlayerGoldDao {
    public void calculateGold(Map<String,Object> map);
}
