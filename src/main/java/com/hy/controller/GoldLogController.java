package com.hy.controller;


import com.alibaba.fastjson.JSON;
import com.hy.common.ApplicationBase;
import com.hy.common.JsonMapper;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.EquipmentLog;
import com.hy.entity.GoldLog;
import com.hy.service.IEquipmentLogService;
import com.hy.service.IGoldLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Gold")
public class GoldLogController {

    @Resource
    private IGoldLogService iGoldLogService;


    @RequestMapping(value = "/showGoldLog")
    public String showGoldLog(HttpSession session, ModelMap model) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/gold/goldLogInfo";
    }

    @ResponseBody
    @RequestMapping(value = "/goldLog_render_list")
    public String showGoldLogList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        //验证登录
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "PlatForm/";
        }
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        String page = "1";
        String pagesize = "10";
        if (reqParam != null && reqParam.get("page") != null) {
            page = (String) reqParam.get("page");
            if (StringUtils.isEmpty(page)) page = "1";
            reqParam.put("pagenum", page);
        }
        if (reqParam != null && reqParam.get("limit") != null) {
            pagesize = (String) reqParam.get("limit");
            if (StringUtils.isEmpty(pagesize)) pagesize = "10";
            reqParam.put("pagesize", pagesize);
        }

        List<GoldLog> logsList = iGoldLogService.getList(reqParam);
        Integer listCount = iGoldLogService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(logsList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/qryGoldLog")
    public String qryGoldLog(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,GoldLog goldLog) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        if(goldLog==null && goldLog.getId() ==""){
            return "ID 不能为空";
        }
        GoldLog goldLog1 = iGoldLogService.getGoldLog(goldLog);
        //调用service
        model.put("goldlog", goldLog1);
        return "platform/gold/goldLogInfo";
    }




}

