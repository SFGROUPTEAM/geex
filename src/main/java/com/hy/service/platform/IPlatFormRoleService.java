package com.hy.service.platform;

import com.hy.entity.PlatFormRole;
import com.hy.entity.PlatFormRole1;
import com.hy.entity.PlatFormRole;
import com.hy.entity.PlatFormRole1;

import java.util.List;
import java.util.Map;

/**
 * 平台角色Service
 */
public interface IPlatFormRoleService {
    public PlatFormRole getPlatFormRole(PlatFormRole platFormRole);

    public List<PlatFormRole> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormRole platFormRole);

    public Integer update(PlatFormRole platFormRole);

    public Integer delete(PlatFormRole platFormRole);

    public List<PlatFormRole1> qryUserRoleList(Map<String, Object> map);

    public Integer getListCount1(Map<String, Object> map);


}
