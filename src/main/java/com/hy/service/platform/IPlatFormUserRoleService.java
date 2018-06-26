package com.hy.service.platform;

import com.hy.entity.PlatFormUserRole;
import com.hy.entity.PlatFormUserRole;

import java.util.List;
import java.util.Map;

public interface IPlatFormUserRoleService {
    public PlatFormUserRole getPlatFormUserRole(PlatFormUserRole platFormUserRole);

    public List<PlatFormUserRole> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormUserRole platFormUserRole);

    public Integer update(PlatFormUserRole platFormUserRole);

    public Integer delete(PlatFormUserRole platFormUserRole);
}
