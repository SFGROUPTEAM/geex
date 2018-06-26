package com.hy.dao;

import com.hy.entity.FriendLink;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 友情链接
 */
@Repository
public interface IFriendLinkDao {
    FriendLink getFriendLink(FriendLink friendLink);

    List<FriendLink> getList(Map<String, Object> map);

    Integer getListCount(Map<String, Object> map);

    Integer add(FriendLink friendLink);

    Integer update(FriendLink friendLink);

    Integer delete(FriendLink friendLink);
}
