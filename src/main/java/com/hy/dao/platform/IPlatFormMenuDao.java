package com.hy.dao.platform;

import com.hy.entity.PlatFormMenu;
import com.hy.entity.PlatFormMenu1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 */
@Repository
public interface IPlatFormMenuDao {
    public PlatFormMenu getPlatFormMenu(PlatFormMenu platFormMenu);

    public List<PlatFormMenu> getList(Map<String, Object> map);
    public List<PlatFormMenu> getUserList(Map<String, Object> map);
    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormMenu platFormMenu);

    public Integer update(PlatFormMenu platFormMenu);

    public Integer delete(PlatFormMenu platFormMenu);

    public List<PlatFormMenu1> getRoleMenuList(Map<String, Object> map);

}
