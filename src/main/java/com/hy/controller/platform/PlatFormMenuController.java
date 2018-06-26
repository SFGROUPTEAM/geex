package com.hy.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.hy.entity.*;
import com.hy.service.platform.IPlatFormButtonService;
import com.hy.service.platform.IPlatFormMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/PMenu")
public class PlatFormMenuController {

    @Resource
    private IPlatFormMenuService platFormMenuService;
    @Resource
    private IPlatFormButtonService platFormButtonService;
    @ResponseBody
    @RequestMapping(value = "/menuTree")
    public String doList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                         @RequestParam Map<String, Object> params, String roleid) {
        Map<String,Object> map=new HashMap<String,Object>();
        roleid= roleid==null?"":roleid;
        map.put("roleid", roleid);
        List<PlatFormMenu1> menuList=platFormMenuService.getRoleMenuList(map);
        List<PlatFormMenu1> nodelist=PlatFormMenu1.buildByRecursive(menuList);
        String nodeInfo= JSONObject.toJSONString(nodelist);
        nodeInfo=nodeInfo.replace("name","title").replace("id","value");
        return nodeInfo;
    }
    @ResponseBody
    @RequestMapping(value = "/buttonTree")
    public String buttonTree(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                         @RequestParam Map<String, Object> params, String roleid) {
        Map<String,Object> map=new HashMap<String,Object>();
        roleid= roleid==null?"":roleid;
        map.put("roleid", roleid);
        map.put("type", "1");
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("roleid",roleid);
        map1.put("menuid","");
        List<PlatFormMenu> menuList=platFormMenuService.getList(map);
        List<PlatFormButton1> buttonList =platFormButtonService.getRoleButtonList(map1);
        List<PlatFormMenu2> nodelist= PlatFormMenu2.buildByRecursive(menuList,buttonList);
        String nodeInfo= JSONObject.toJSONString(nodelist);
        nodeInfo=nodeInfo.replace("name","title").replace("id","value");
        return nodeInfo;
    }

}
