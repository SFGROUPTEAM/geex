package com.hy.dao.platform;

import com.hy.entity.PlatFormRoleButton;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 平台角色按钮关联
 */
@Repository
public interface IPlatFormRoleButtonDao {
    public PlatFormRoleButton getPlatFormRoleButton(PlatFormRoleButton platFormRoleButton);

    public List<PlatFormRoleButton> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormRoleButton platFormRoleButton);

    public Integer update(PlatFormRoleButton platFormRoleButton);

    public Integer delete(PlatFormRoleButton platFormRoleButton);
}
