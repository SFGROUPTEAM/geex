package com.hy.service;

import com.hy.entity.FriendLink;

import java.util.List;
import java.util.Map;

/**
 * 新闻
 */
public interface IFriendLinkService {

    FriendLink getFriendLink(FriendLink friendLink);

    List<FriendLink> getList(Map<String, Object> map);

    Integer getListCount(Map<String, Object> map);

    Integer add(FriendLink friendLink);

    Integer update(FriendLink friendLink);

    Integer delete(FriendLink friendLink);
}
