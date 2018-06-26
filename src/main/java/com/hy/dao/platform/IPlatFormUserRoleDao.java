package com.hy.dao.platform;

import com.hy.entity.PlatFormUserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 平台用户角色关联
 */
@Repository
public interface IPlatFormUserRoleDao {
    public PlatFormUserRole getPlatFormUserRole(PlatFormUserRole platFormUserRole);

    public List<PlatFormUserRole> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormUserRole platFormUserRole);

    public Integer update(PlatFormUserRole platFormUserRole);

    public Integer delete(PlatFormUserRole platFormUserRole);
}
