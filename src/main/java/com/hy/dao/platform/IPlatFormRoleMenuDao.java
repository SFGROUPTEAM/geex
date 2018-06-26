package com.hy.dao.platform;

import com.hy.entity.PlatFormRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 平台角色菜单关联
 */
@Repository
public interface IPlatFormRoleMenuDao {
    public PlatFormRoleMenu getPlatFormRoleMenu(PlatFormRoleMenu platFormRoleMenu);

    public List<PlatFormRoleMenu> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormRoleMenu platFormRoleMenu);

    public Integer update(PlatFormRoleMenu platFormRoleMenu);

    public Integer delete(PlatFormRoleMenu platFormRoleMenu);

}
