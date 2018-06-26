package com.hy.dao.platform;

import com.hy.entity.PlatFormUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Lijianguo on 2018/4/24.
 * 平台用户Dao
 */
@Repository
public interface IPlatFormUserDao {

    public PlatFormUser getPlatFormUser(PlatFormUser platFormUser);

    public List<PlatFormUser> getList(Map<String, Object> map);

    public Integer getListCount(Map<String, Object> map);

    public Integer add(PlatFormUser platFormUser);

    public Integer update(PlatFormUser platFormUser);

    public Integer delete(PlatFormUser platFormUser);

    public Integer updatePwd(PlatFormUser platFormUser);

}
