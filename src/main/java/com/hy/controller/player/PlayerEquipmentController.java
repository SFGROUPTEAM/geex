package com.hy.controller.player;

import com.alibaba.fastjson.JSON;
import com.hy.common.SessionContants;
import com.hy.entity.*;
import com.hy.service.IEquipmentLogService;
import com.hy.service.IEquipmentService;
import com.hy.service.player.IPlayerEquipmentService;
import com.hy.service.player.IPlayerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Player")
public class PlayerEquipmentController {

    @Autowired
    IPlayerUserService playerUserService;

    @Autowired
    IPlayerEquipmentService playerEquipmentService;

    @Autowired
    IEquipmentLogService equipmentLogService;

    @RequestMapping({"/equipment","/equipment.html"})
    public String equipment(HttpServletRequest request,HttpSession session,ModelMap model, @RequestParam Map<String,Object> param){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player pUser = JSON.parseObject(jsonString, Player.class);

        String pagenum=param.containsKey("pagenum")?param.get("pagenum").toString():"1";
        String pagesize="10";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("playerid",pUser.getId());
        map.put("pagenum",pagenum);
        map.put("pagesize",pagesize);

        List<PlayerEquipment1> list=playerEquipmentService.getList(map);
        Integer listCount=playerEquipmentService.getListCount(map);
        model.put("list",list);
        model.put("listCount",listCount);
        model.put("pagenum",pagenum);
        model.put("pagesize",pagesize);

        return "player/equipmentlist";
    }


    @RequestMapping({"/exchangelog","/exchangelog.html"})
    public String exchangeLog(HttpServletRequest request, HttpSession session, ModelMap model, @RequestParam Map<String,Object> param){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player1 pUser = JSON.parseObject(jsonString, Player1.class);

        String pagenum=param.containsKey("pagenum")?param.get("pagenum").toString():"1";
        String pagesize="10";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("playerid",pUser.getId());

        map.put("pagenum",pagenum);
        map.put("pagesize",pagesize);

        map.put("data1",param.containsKey("data1")?param.get("data1"):"");
        map.put("data2",param.containsKey("data2")?param.get("data2"):"");

        List<EquipmentLog1> list=equipmentLogService.getList(map);
        Integer listCount=equipmentLogService.getListCount(map);
        model.put("list",list);
        model.put("listCount",listCount);
        model.put("pagenum",pagenum);
        model.put("pagesize",pagesize);

        return "player/exchangeloglist";
    }


}
