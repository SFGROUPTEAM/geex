package com.hy.controller;


import com.alibaba.fastjson.JSON;
import com.hy.entity.*;
import com.hy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller

@RequestMapping("/")
public class IndexController {

    @Autowired
    IGameInfoService gameInfoService;
    @Autowired
    IGameCategoryService gameCategoryService;

    @Autowired
    LocaleResolver localeResolver;

    @Autowired
    IFriendLinkService friendLinkService;

    @Autowired
    IEquipmentService equipmentService;

    @Autowired
    IBaseConfigService baseConfigService;


    @RequestMapping({"/", "/index.html", "/index.jsp"})
    public String index1(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {

        //游戏分类
        Map<String, Object> gameCategoryMap = new HashMap<String, Object>();
        gameCategoryMap.put("pagenum", "1");
        gameCategoryMap.put("pagesize", "5");
        gameCategoryMap.put("state", "1");
        gameCategoryMap.put("parentid", "00");
        List<GameCategory1> gameCategoryList = gameCategoryService.getList(gameCategoryMap);

        //游戏
        Map<String, List<GameInfo2>> gameListMap = new HashMap<String, List<GameInfo2>>();
        for (GameCategory1 category1 : gameCategoryList) {
            Map<String, Object> gameMap = new HashMap<String, Object>();
            gameMap.put("pagenum", "1");
            gameMap.put("pagesize", "18");
            gameMap.put("state", "1");
            gameMap.put("status", "1");
            gameMap.put("topCategoryId", category1.getId());
            List<GameInfo2> gameList = gameInfoService.getAllGameList(gameMap);
            gameListMap.put(category1.getId(), gameList);
        }
        //获取装备信息
        Map<String, Object> equipmentMap = new HashMap<String, Object>();
        equipmentMap.put("state", 1);
        List<Equipment1> equipmentList = equipmentService.getAllEquipmentList(equipmentMap);
        Integer equipmentInfoListCount = equipmentService.getAllEquipmentCount(equipmentMap);

        /*for(GameEquipmentCategory1 category1:gameEquipmentCategoryList){
            Map<String,Object> gameEqipmentMap=new HashMap<String,Object>();
               gameEqipmentMap.put("pagenum","1");
            gameEqipmentMap.put("pagesize","6");
            gameEqipmentMap.put("state","1");
            gameEqipmentMap.put("status","0");
            gameEqipmentMap.put("topCategoryId",category1.getId());
            List<GameEquipment2> gameEquipmentList=gameEquipmentService.getEquipmentList(gameEqipmentMap);
            gameEquipmentListMap.put(category1.getId(),gameEquipmentList);
        }*/

        //友情链接
        Map<String, Object> friendLinkMap = new HashMap<String, Object>();
        friendLinkMap.put("pagenum", "1");
        friendLinkMap.put("pagesize", "20");
        friendLinkMap.put("state", "1");
        friendLinkMap.put("status", "1");
        List<FriendLink> friendLinkList = friendLinkService.getList(friendLinkMap);

        model.put("gameCategoryList", gameCategoryList);

        model.put("gameListMap", gameListMap);
        model.put("equipmentList", equipmentList);
        session.setAttribute("friendLinkList", com.alibaba.fastjson.JSONObject.toJSON(friendLinkList));
        return "index";
    }
    @RequestMapping("/baseConfig")
    @ResponseBody
    public String baseConfig(HttpSession session) {
        //基础配置
        BaseConfig baseConfig = baseConfigService.getBaseConfig();
        session.setAttribute("baseConfig",com.alibaba.fastjson.JSONObject.toJSON(baseConfig));
        return com.alibaba.fastjson.JSONObject.toJSONString(baseConfig);
    }
    @RequestMapping("/friendLink")
    @ResponseBody
    public String friendLink(HttpSession session) {
        //友情链接
        Map<String, Object> friendLinkMap = new HashMap<String, Object>();
        friendLinkMap.put("pagenum", "1");
        friendLinkMap.put("pagesize", "20");
        friendLinkMap.put("state", "1");
        friendLinkMap.put("status", "1");
        List<FriendLink> friendLinkList = friendLinkService.getList(friendLinkMap);
        session.setAttribute("friendLinkList", com.alibaba.fastjson.JSONObject.toJSON(friendLinkList));
        return com.alibaba.fastjson.JSONObject.toJSONString(friendLinkList);
    }

    @RequestMapping("/changeLanguage")
    @ResponseBody
    public String i18nChange(HttpServletRequest request, HttpServletResponse response, String lang) {
        HttpSession session = request.getSession();

        if ("zh_cn".equals(lang)) {
            Locale locale = new Locale("zh", "CN");
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
            session.setAttribute("lang", "zh_CN");
        } else if ("en_us".equals(lang)) {
            Locale locale = new Locale("en", "US");
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
            session.setAttribute("lang", "en_US");
        } else {
            session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, LocaleContextHolder.getLocale());
            session.setAttribute("lang", "zh_CN");
        }
        return JSON.toJSONString("success");
    }

}

