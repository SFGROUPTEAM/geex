package com.hy.dao.player;

import com.hy.entity.ExAsset;
import com.hy.entity.ExUser;
import com.hy.entity.Player;
import com.hy.entity.Player1;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IPlayerUserDao {

        public Player1 getPlayerUser(Player player);

        public List<Player> getList(Map<String, Object> map);

        public Integer getListCount(Map<String, Object> map);

        public Integer add(Player player);

        public Integer update(Player player);

        public void login(Map<String,Object> map);

        public Integer exchange(Map<String, Object> map);

        public ExUser getExUser(ExUser exUser);

        public Integer addExUser(ExUser exUser);

        public Integer addExAsset(ExAsset exAsset);
}
