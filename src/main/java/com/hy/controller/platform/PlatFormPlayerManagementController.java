package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.hy.common.ApplicationBase;
import com.hy.common.JsonMapper;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.Player;
import com.hy.service.player.IPlayerUserService;
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
@RequestMapping("/platformPlayer")
public class PlatFormPlayerManagementController {
    @Resource
    private IPlayerUserService playerUserService;
    @RequestMapping("/showPlayerInfo")
    public String showGameInfo(HttpSession session, ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/playerManagement/playerList";
    }
    @ResponseBody
    @RequestMapping("/player_render_list")
    public String showPlayerOnPlatform(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
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
        List<Player> supplierList = playerUserService.getList(reqParam);
        Integer listCount = playerUserService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(supplierList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
