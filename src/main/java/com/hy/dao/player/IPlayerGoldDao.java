package com.hy.dao.player;

import com.hy.entity.PlayerEquipment;
import com.hy.entity.PlayerEquipment1;
import com.hy.entity.PlayerGold;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IPlayerGoldDao {

        public PlayerGold getPlayerGold(PlayerGold playerGold);

        public List<PlayerGold> getList(Map<String, Object> map);

        public Integer getListCount(Map<String, Object> map);

}
