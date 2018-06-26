package com.hy.service.platform;


import com.hy.entity.PlatFormRoleMenu;
import com.hy.entity.PlatFormRoleMenu;

import java.util.List;
import java.util.Map;

public interface IPlatFormRoleMenuService {
    public PlatFormRoleMenu getPlatFormRoleMenu(PlatFormRoleMenu platFormRoleMenu);

    public List<PlatFormRoleMenu> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormRoleMenu platFormRoleMenu);

    public Integer update(PlatFormRoleMenu platFormRoleMenu);

    public Integer delete(PlatFormRoleMenu platFormRoleMenu);
}
