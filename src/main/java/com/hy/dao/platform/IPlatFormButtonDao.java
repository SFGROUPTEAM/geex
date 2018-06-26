package com.hy.dao.platform;

import com.hy.entity.PlatFormButton;
import com.hy.entity.PlatFormButton1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 平台按钮管理
 */
@Repository
public interface IPlatFormButtonDao {
    public PlatFormButton getPlatFormButton(PlatFormButton platFormButton);

    public List<PlatFormButton> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormButton platFormButton);

    public Integer update(PlatFormButton platFormButton);

    public Integer delete(PlatFormButton platFormButton);

    public List<PlatFormButton1> getRoleButtonList(Map<String, Object> map);

}
