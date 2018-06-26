package com.hy.service.player;

import com.hy.entity.PlayerEquipment;
import com.hy.entity.PlayerEquipment1;
import com.hy.entity.PlayerGold;

import java.util.List;
import java.util.Map;

public interface IPlayerGoldService {

        public PlayerGold getPlayerGold(PlayerGold playerGold);

        public List<PlayerGold> getList(Map<String, Object> map);

        public Integer getListCount(Map<String, Object> map);

}
