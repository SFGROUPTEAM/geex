package com.hy.controller.player;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.ApplicationBase;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.*;
import com.hy.service.IGoldLogService;
import com.hy.service.player.IPlayerGoldService;
import com.hy.service.player.IPlayerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Player")
public class PlayerGoldController {

    @Autowired
    IPlayerUserService playerUserService;

    @Autowired
    IPlayerGoldService playerGoldService;

    @Autowired
    IGoldLogService goldLogService;

    @RequestMapping({"/gold","/gold.html"})
    public String gold(HttpServletRequest request,HttpSession session,ModelMap model, @RequestParam Map<String,Object> param){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player1 pUser = JSON.parseObject(jsonString, Player1.class);

        PlayerGold playerGold=new PlayerGold();
        playerGold.setPlayerid(pUser.getId());
        playerGold=playerGoldService.getPlayerGold(playerGold);
        if(playerGold!=null) {
            BigDecimal cnt = new BigDecimal(playerGold.getGoldcnt());
            DecimalFormat df = new DecimalFormat(",###,##0.00"); //保留一位小数
            String goldcnt = String.valueOf(df.format(cnt));
            playerGold.setGoldcnt(goldcnt);
        }else{
            playerGold=new PlayerGold();
            playerGold.setPlayerid(pUser.getId());
            BigDecimal cnt = new BigDecimal("0");
            DecimalFormat df = new DecimalFormat(",###,##0.00"); //保留一位小数
            String goldcnt = String.valueOf(df.format(cnt));
            playerGold.setGoldcnt(goldcnt);
        }

        model.put("playerGold",playerGold);

        String pagenum=param.containsKey("pagenum")?param.get("pagenum").toString():"1";
        String pagesize="10";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("playerid",pUser.getId());

        map.put("pagenum",pagenum);
        map.put("pagesize",pagesize);
        map.put("data1",param.containsKey("data1")?param.get("data1"):"");
        map.put("data2",param.containsKey("data2")?param.get("data2"):"");

        List<GoldLog> goldLogList=goldLogService.getList(map);
        Integer listCount=goldLogService.getListCount(map);

        model.put("goldLogList",goldLogList);
        model.put("listCount",listCount);
        model.put("pagenum",pagenum);
        model.put("pagesize",pagesize);

        return "player/goldlist";
    }



}
