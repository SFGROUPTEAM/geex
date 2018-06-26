package com.hy.controller;


import com.alibaba.fastjson.JSONArray;
import com.hy.common.JsonMapper;
import com.hy.entity.GameInfo;
import com.hy.entity.News;
import com.hy.service.INewsService;
import com.hy.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class NewsController {

    @Autowired
    INewsService newsService;

    @ResponseBody
    @RequestMapping("/news_list")
    public String getNewsListIn(HttpServletRequest request, HttpSession session, ModelMap model){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("state",1);
        map.put("status",1);
        map.put("type",0);
        map.put("pagenum","1");
        map.put("pagesize","10");
        List<News> list=newsService.getList(map);
        String resultJson="";
        resultJson = JsonMapper.toJsonString(list);
        model.put("list",list);
        return resultJson;

    }

    @RequestMapping("/newsList.html")
    public String getNewsList(HttpServletRequest request, HttpSession session, ModelMap model){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("state",1);
        map.put("status",1);
        map.put("type",0);
        map.put("pagenum",1);
        map.put("pagesize",30);

        List<News> newsList=newsService.getList(map);
        int newsListCount=newsService.getListCount(map);

        model.put("newsList", newsList);
        model.put("newsListCount",newsListCount);
        return "newslist";
    }
    @ResponseBody
    @RequestMapping("/getNewsByPage")
    public String getNewsByPage(HttpServletRequest request, HttpSession session, ModelMap model,String pagenum){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("state",1);
        map.put("status",1);
        map.put("type",0);
        map.put("pagenum",pagenum);
        map.put("pagesize",30);
        List<News> newsList=newsService.getList(map);
        int newsListCount=newsService.getListCount(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("newsList",newsList);
        map1.put("newsListCount",newsListCount);
        String resultJson="";
        resultJson = JsonMapper.toJsonString(map1);
        return resultJson;

    }
    @RequestMapping("/news.html")
    public String getNewsInfo(HttpServletRequest request, HttpSession session, ModelMap model,News news){
        News newsDetail = newsService.getNews(news);
        model.put("news", newsDetail);
        return "news";
    }

}

