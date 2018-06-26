package com.hy.service.platform;


import com.hy.entity.PlatFormMenu;
import com.hy.entity.PlatFormMenu1;
import com.hy.entity.PlatFormMenu;
import com.hy.entity.PlatFormMenu1;

import java.util.List;
import java.util.Map;

public interface IPlatFormMenuService {

    public PlatFormMenu getPlatFormMenu(PlatFormMenu platFormMenu);

    public List<PlatFormMenu> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormMenu platFormMenu);

    public Integer update(PlatFormMenu platFormMenu);

    public Integer delete(PlatFormMenu platFormMenu);
    /**
     *获取用户菜单
     * */
    public List<PlatFormMenu> getUserList(Map<String, Object> map);

    public List<PlatFormMenu1> getRoleMenuList(Map<String, Object> map);

}
