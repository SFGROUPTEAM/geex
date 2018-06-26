package com.hy.controller;


import com.alibaba.fastjson.JSON;
import com.hy.common.StringUtils;
import com.hy.entity.Equipment1;
import com.hy.service.IEquipmentService;
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
public class EquipmentController {

    @Autowired
    IEquipmentService equipmentService;


    @RequestMapping("/equipmentList.html")
    public String getEquipmentList(HttpServletRequest request, HttpSession session, ModelMap model) {

        //获取装备信息
        Map<String,Object> equipmentMap=new HashMap<String,Object>();
        equipmentMap.put("state",1);
        List<Equipment1> equipmentList = equipmentService.getAllEquipmentList(equipmentMap);
        Integer equipmentInfoListCount = equipmentService.getAllEquipmentCount(equipmentMap);
        model.put("equipmentList",equipmentList);
        model.put("equipmentInfoListCount",equipmentInfoListCount);
        return "equipmentlist";
    }

    @RequestMapping("/equipmentdetail.html")
    public String getEquipmentDetail(HttpServletRequest request, HttpSession session, ModelMap model, @RequestParam Map<String,Object> param) {
        String equipmentId=param.containsKey("equipmentId")?param.get("equipmentId").toString():"";
        // equipmentId="790575dbd6eb47c0825ba6dd308115e3";//临时指定
        //获取装备信息
        Equipment1 equipment1=new Equipment1();
        equipment1.setId(equipmentId);
        equipment1=equipmentService.getEquipment1(equipment1);
        model.put("equipment",equipment1);

        return "equipmentdetail";
    }

    //前端获取所有装备json数据
    @ResponseBody
    @RequestMapping("/getEquipmentListUsedByAPP")
    public String getEquipmentListUsedByAPP(HttpServletRequest request, HttpSession session, ModelMap model) {

        //获取装备信息
        Map<String,Object> equipmentMap=new HashMap<String,Object>();
        equipmentMap.put("state",1);
        List<Equipment1> equipmentList = equipmentService.getAllEquipmentList(equipmentMap);
        return JSON.toJSONString(equipmentList);
    }
    //0前端获取所有装备json数据
    @ResponseBody
    @RequestMapping("/getDetailEquipmentUsedByAPP")
    public String getDetailEquipmentUsedByAPP(HttpServletRequest request, HttpSession session, ModelMap model, @RequestParam Map<String,Object> param) {
        String equipmentId=param.containsKey("equipmentId")?param.get("equipmentId").toString():"";
        // equipmentId="790575dbd6eb47c0825ba6dd308115e3";//临时指定
        //获取装备信息
        Equipment1 equipment1=new Equipment1();
        equipment1.setId(equipmentId);
        equipment1=equipmentService.getEquipment1(equipment1);

        return JSON.toJSONString(equipment1);
    }
}

