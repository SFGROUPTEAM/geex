package com.hy.service.platform;

import com.hy.entity.PlatFormRoleButton;
import com.hy.entity.PlatFormRoleButton;

import java.util.List;
import java.util.Map;

public interface IPlatFormRoleButtonService {

    public PlatFormRoleButton getPlatFormRoleButton(PlatFormRoleButton platFormRoleButton);

    public List<PlatFormRoleButton> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormRoleButton platFormRoleButton);

    public Integer update(PlatFormRoleButton platFormRoleButton);

    public Integer delete(PlatFormRoleButton platFormRoleButton);
}
