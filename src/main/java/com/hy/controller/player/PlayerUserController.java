package com.hy.controller.player;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.ApplicationBase;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.ExUser;
import com.hy.entity.Player;
import com.hy.entity.Player1;
import com.hy.entity.PlayerEquipment1;
import com.hy.listener.AsyncTask;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Player")
public class PlayerUserController {

    @Autowired
    IPlayerUserService playerUserService;
    @Autowired
    private AsyncTask asyncTask;
    //注册
    @RequestMapping({"/register","/register.html"})
    public String register(HttpServletRequest request,HttpSession session,ModelMap model){

        return "player/register";
    }


    //注册提交
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String commitRegister(HttpServletRequest request,HttpSession session,ModelMap model,@RequestParam Map<String, Object> params){
        try {
            String resultStr = "";
            String username = params.containsKey("username") ? params.get("username").toString() : "";
            String password = params.containsKey("password") ? params.get("password").toString() : "";
            String kaptcha = params.containsKey("kaptcha") ? params.get("kaptcha").toString() : "";//验证码
            if (StringUtils.isBlank(username)) {
                resultStr = "用户名不能为空";
            } else if (StringUtils.isBlank(password)) {
                resultStr = "密码不能为空";
            } else if (!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY).toString())) {
                resultStr = "验证码不正确";
            }

            if (!StringUtils.isBlank(resultStr)) {
                model.put("errorInfo", resultStr);
                request.setAttribute("errorInfo", resultStr);
                return "player/register";
            }
            String registerip = ApplicationBase.getClientIP(request);
            Player player = new Player();
            player.setRegisterip(registerip);
            player.setUsername(username);
            player.setPassword(password);
            playerUserService.add(player);
            Player pUser = playerUserService.getPlayerUser(player);

            model.put("errorInfo", "Register_Success");

        }catch (Exception e){
            e.printStackTrace();
            // 提示信息
            request.setAttribute("errorInfo", "注册出现错误！");
            return "player/register";
        }
        return "player/login";
    }


    //找回密码
    @RequestMapping({"/findPassword"})
    public String findpassword(HttpServletRequest request, HttpSession session, ModelMap model) {
        return "player/findPassword";
    }

    /**
     * 校验用户名是否已注册
     */
    @ResponseBody
    @RequestMapping(value="/checkUser")
    public String checkPlayerUser(HttpServletRequest request,ModelMap model,HttpSession session,String username)throws ServletException, IOException {
        if(StringUtils.isEmpty(username)){
            return "false";
        }
        Player player=new Player();
        player.setUsername(username.replace(",",""));
        Player pUser = playerUserService.getPlayerUser(player);

        ExUser exUser=new ExUser();
        exUser.setUsername(username.replace(",",""));
        exUser = playerUserService.getExUser(exUser);
        if(StringUtils.isEmpty(pUser) && StringUtils.isEmpty(exUser)){
            return "true";
        }

        return "false";
    }

    //找回密码校验用户名与验证码
    @ResponseBody
    @RequestMapping(value="/check_user_captcha",method = RequestMethod.POST)
    public String checkCaptcha(HttpServletRequest request,ModelMap model,HttpSession session,
                               HttpServletResponse response)throws ServletException, IOException {
        String loginName=request.getParameter("loginName");
        Player player=new Player();
        player.setUsername(loginName);
        Player pUser = playerUserService.getPlayerUser(player);
        if(StringUtils.isEmpty(pUser)){
            return "用户不存在或是无效用户";
        }

        String kaptcha = request.getParameter("kaptcha"); //验证码
        if(!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY).toString())){
            return "无效验证码";
        }

        return "SUCCESS";
    }


    //找回重置密码
    @ResponseBody
    @RequestMapping(value = "/save_newPwd", method = RequestMethod.POST)
    public String resetPassword(HttpServletRequest request,HttpSession session,ModelMap model,@RequestParam Map<String, Object> params){
        String resultStr="";
        String loginName=params.containsKey("loginName")?params.get("loginName").toString():"";
        String newPassword=params.containsKey("newPassword")?params.get("newPassword").toString():"";;
        if(StringUtils.isBlank(loginName)){
            resultStr="手机号不能为空";
        }

        if(!StringUtils.isBlank(resultStr)){
            return resultStr;
        }

        Player player = new Player();
        player.setUsername(loginName);
        player.setPhone(loginName);
        player.setPassword(newPassword);
        playerUserService.update(player);

        return "SUCCESS";
    }


    //用户服务协议
    @RequestMapping({"/agreement","/agreement.html"})
    public String agreement(HttpServletRequest request,HttpSession session,ModelMap model){

        return "common/agreement";
    }

    ///会员中心首页
    @RequestMapping("/main")
    public String playerAccountList(HttpServletRequest request,HttpSession session,ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player1 puser = JSON.parseObject(jsonString, Player1.class);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("playerid",puser.getId());

        return "player/main";
    }

    //我的资料
    @RequestMapping({"/main","/main.html"})
    public String information(HttpServletRequest request,HttpSession session,ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player pUser = JSON.parseObject(jsonString, Player.class);
        pUser=playerUserService.getPlayerUser(pUser);
        model.put("user",pUser);

        return "player/main";
    }

    //修改完善我的资料
    @RequestMapping(value = "/main.html",method=RequestMethod.POST)
    public String equipment(HttpServletRequest request,HttpSession session,ModelMap model,Player player){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "player/login";
        }

        playerUserService.update(player);
        player=playerUserService.getPlayerUser(player);
        model.put("user",player);
        model.put("isFreshFlag", "Y");

        return "player/main";
    }

    //兑换道具
    @ResponseBody
    @RequestMapping(value = "/exchange.html",method = RequestMethod.POST)
    public String addShoppingCart(HttpServletRequest request, HttpSession session, ModelMap model, @RequestParam Map<String, Object> params){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (operUser == null) {
            return "isLoginOut";
        }
        String jsonString = JSON.toJSONString(operUser);
        Player1 pUser = JSON.parseObject(jsonString, Player1.class);

        Map<String,Object> data=new HashMap<String,Object>();
        data.put("p_playerId",pUser.getId());
        data.put("p_equipmentId",params.get("equirmentid"));
        data.put("p_quantity",params.get("num"));
        playerUserService.exchange(data);

        Map<String,Object> result=new HashMap<String,Object>();
        String retArr[]=String.valueOf(data.get("p_retMsg")).split("\\|");
        if(data.get("p_retVal").equals("1")){
            result.put("retVal","00");
            result.put("retMsg",retArr[0]);
        }else{
            result.put("retVal","01");
            result.put("retMsg",retArr[0]);
        }
        //刷新session最后剩余金币
        String playGoldCnt=retArr.length>1?retArr[1]:pUser.getGoldcnt();
        pUser = playerUserService.getPlayerUser(pUser);
        session.setAttribute(SessionContants.PLAYERUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(pUser));



        String str= JSONObject.toJSONString(result);
        return str;
    }
}
