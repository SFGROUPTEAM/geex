package com.hy.controller;

import com.alibaba.fastjson.JSON;
import com.hy.entity.*;
import com.hy.service.IGameCategoryService;
import com.hy.service.IGameInfoService;
import com.hy.service.IGamePicService;
import net.sf.json.JSONArray;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class GameInfoController {

    @Autowired
    IGameInfoService gameInfoService;
    @Autowired
    IGameCategoryService gameCategoryService;
    @Autowired
    IGamePicService gamePicService;

    @RequestMapping("/gameList.html")
    public String getGameList(HttpServletRequest request,HttpSession session,ModelMap model){
        //找游戏分类
        Map<String,Object> gameCategoryMap=new HashMap<String,Object>();
        gameCategoryMap.put("state",1);
        gameCategoryMap.put("parentid","00");
        List<GameCategory1> gameCategoryList = gameCategoryService.getList(gameCategoryMap);
        //找20个游戏
        Map<String,Object> gameInfoMap=new HashMap<String,Object>();
        gameInfoMap.put("status",1);
        gameInfoMap.put("state",1);
        gameInfoMap.put("pagesize",20);
        gameInfoMap.put("pagenum",1);
        List<GameInfo2> gameInfoList = gameInfoService.getAllGameList(gameInfoMap);
        Integer gameInfoListCount = gameInfoService.getAllGameCount(gameInfoMap);
        model.put("gameCategoryList",gameCategoryList);
        model.put("gameInfoList",gameInfoList);
        model.put("gameInfoListCount",gameInfoListCount);
        return "gamelist";
    }
    @ResponseBody
    @RequestMapping("/getGameListByPageNumAndCategory")
    public String getGameListByPageNumAndCategory(HttpServletRequest request,HttpSession session,ModelMap model,String categoryid,String pagenum,String name){
        Map<String,Object> gameInfoMap=new HashMap<String,Object>();
        gameInfoMap.put("status",1);
        gameInfoMap.put("state",1);
        gameInfoMap.put("pagesize",20);
        gameInfoMap.put("pagenum",pagenum);
        gameInfoMap.put("ID",categoryid);
        gameInfoMap.put("nameOrRemark",name);
        List<GameInfo2> gameInfoList = gameInfoService.getclassGameList(gameInfoMap);
        Integer gameInfoListCount = gameInfoService.getClassGameListCount(gameInfoMap);
        Map<String,Object> map = new HashMap<>();
        map.put("gameInfoList",gameInfoList);
        map.put("gameInfoListCount",gameInfoListCount);
        return JSONArray.fromObject(map).toString();
    }

    //获取最新游戏列表
    @ResponseBody
    @RequestMapping("/getNewGameList")
    public String getNewGame(HttpServletRequest request,HttpSession session,ModelMap model,GameInfo gameInfo){
        Map<String,Object>map=new HashMap<String,Object>();
        List<Map<String,Object>>list=gameInfoService.getNewList(map);

        JSONArray json=new JSONArray();
        String resultJson="";
        resultJson=JSONArray.fromObject(list).toString();
        return resultJson;
    }


    @RequestMapping("/gamedetail.html")
    public String getEquipmentDetail(HttpServletRequest request, HttpSession session, ModelMap model, @RequestParam Map<String,Object> param) {
        String gameId=param.containsKey("gameId")?param.get("gameId").toString():"";
        //获取游戏信息
        GameInfo1 gameInfo=new GameInfo1();
        gameInfo.setId(gameId);
        gameInfo=gameInfoService.getGameInfo(gameInfo);
        Map<String,Object> map = new HashMap<>();
        map.put("gameid",gameId);
        map.put("usetype",2);
        map.put("pagesize",1);
        map.put("pagenum",1);
        List<GamePic1> gamePicList = gamePicService.getList(map);
        if (gamePicList!=null&&gamePicList.size()>0){
            model.put("gamePic",gamePicList.get(0));
        }else {
            model.put("gamePic",new GamePic());
        }
        model.put("gameInfo",gameInfo);
        return "gamedetail";
    }
    @ResponseBody
    @RequestMapping("/getGameListUsedByAPP")
    public String getGameListUsedByAPP(HttpServletRequest request,HttpSession session,ModelMap model,String categoryid,String pagenum,String name){
        Map<String,Object> gameInfoMap=new HashMap<String,Object>();
        gameInfoMap.put("status",1);
        gameInfoMap.put("state",1);
        gameInfoMap.put("ID",categoryid);
        gameInfoMap.put("nameOrRemark",name);
        List<GameInfo2> gameInfoList = gameInfoService.getAllGameList(gameInfoMap);
        return JSON.toJSONString(gameInfoList);
    }
}

