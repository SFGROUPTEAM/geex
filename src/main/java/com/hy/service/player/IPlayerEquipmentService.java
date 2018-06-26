package com.hy.service.player;

import com.hy.entity.PlayerEquipment;
import com.hy.entity.PlayerEquipment1;

import java.util.List;
import java.util.Map;

public interface IPlayerEquipmentService {

        public PlayerEquipment getPlayerEquipment(PlayerEquipment playerEquipment);

        public List<PlayerEquipment1> getList(Map<String, Object> map);

        public Integer getListCount(Map<String, Object> map);

        public Integer add(PlayerEquipment playerEquipment);

        public Integer update(PlayerEquipment playerEquipment);

}
