package com.hy.serviceImpl;

import com.hy.common.StringUtils;
import com.hy.dao.IFriendLinkDao;
import com.hy.entity.FriendLink;
import com.hy.service.IFriendLinkService;
import com.github.pagehelper.PageHelper;
import com.hy.dao.IFriendLinkDao;
import com.hy.service.IFriendLinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class FriendLinkServiceImpl implements IFriendLinkService {

    @Resource
    private IFriendLinkDao friendLinkDao;
    @Override
    public FriendLink getFriendLink(FriendLink friendLink) {
        return friendLinkDao.getFriendLink(friendLink);
    }

    @Override
    public List<FriendLink> getList(Map<String, Object> map) {
        int pagenum=1;
        int pagesize=999999;
        if(map.get("pagenum")!=null && StringUtils.isNumeric(map.get("pagenum").toString()))
            pagenum=Integer.parseInt(map.get("pagenum").toString());
        if(map.get("pagesize")!=null && StringUtils.isNumeric(map.get("pagesize").toString()))
            pagesize=Integer.parseInt(map.get("pagesize").toString());
        PageHelper.startPage(pagenum,pagesize);
        return friendLinkDao.getList(map);
    }

    @Override
    public Integer getListCount(Map<String, Object> map) {
        return friendLinkDao.getListCount(map);
    }

    @Override
    public Integer add(FriendLink friendLink) {
        return friendLinkDao.add(friendLink);
    }

    @Override
    public Integer update(FriendLink friendLink) {
        return friendLinkDao.update(friendLink);
    }

    @Override
    public Integer delete(FriendLink friendLink) {
        return friendLinkDao.delete(friendLink);
    }
}
