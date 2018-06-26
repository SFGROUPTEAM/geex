package com.hy.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.hy.entity.PlatFormButton;
import com.hy.entity.PlatFormButton1;
import com.hy.entity.PlatFormMenu1;
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
@RequestMapping("/PButton")
public class PlatFormButtonController {

    @Resource
    private IPlatFormButtonService platFormButtonService;
    @ResponseBody
    @RequestMapping(value = "/buttonTree")
    public String doList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                         @RequestParam Map<String, Object> params, String menuid,String roleid) {
        Map<String,Object> map=new HashMap<String,Object>();
        menuid= menuid==null?"":menuid;
        roleid= roleid==null?"":roleid;

        map.put("menuid", menuid);
        map.put("roleid", roleid);

        List<PlatFormButton1> menuList=platFormButtonService.getRoleButtonList(map);
        List<PlatFormButton1> nodelist=PlatFormButton1.buildByRecursive(menuList);
        String nodeInfo= JSONObject.toJSONString(nodelist);
        nodeInfo=nodeInfo.replace("name","title").replace("id","value");
        return nodeInfo;
    }






}
