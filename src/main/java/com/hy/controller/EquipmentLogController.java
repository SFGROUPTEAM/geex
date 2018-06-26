package com.hy.controller;


import com.alibaba.fastjson.JSON;
import com.hy.common.ApplicationBase;
import com.hy.common.JsonMapper;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.EquipmentLog;
import com.hy.entity.EquipmentLog1;
import com.hy.service.IEquipmentLogService;
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
@RequestMapping("/EquipmentLog")
public class EquipmentLogController {
    @Resource
    private IEquipmentLogService iEquipmentLogService;


    @RequestMapping(value = "/showEquipmentLog")
    public String showEquipmentLog(HttpSession session, ModelMap model) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/equipment/equipmentLogInfo";
    }

    @ResponseBody
    @RequestMapping(value = "/equipmentLog_render_list")
    public String showEquipmentLogList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
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

        List<EquipmentLog1> logsList = iEquipmentLogService.getList(reqParam);
        Integer listCount = iEquipmentLogService.getListCount(reqParam);
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

    @RequestMapping("/qryEquipmentLog")
    public String qryEquipmentLog(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,EquipmentLog equipmentLog) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        if(equipmentLog==null && equipmentLog.getId() ==""){
            return "ID 不能为空";
        }
        EquipmentLog equipmentLog1 = iEquipmentLogService.getEquipmentLog(equipmentLog);
        //调用service
        model.put("equipment", equipmentLog1);
        return "platform/equipment/equipmentLogInfo";
    }



}

