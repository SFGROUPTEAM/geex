package com.hy.controller.player;

import com.hy.common.StringUtils;
import com.hy.entity.Player;
import com.hy.common.SessionContants;
import com.hy.entity.Player1;
import com.hy.service.player.IPlayerUserService;
import com.alibaba.fastjson.JSON;
//import org.apache.shiro.authc.ExcessiveAttemptsException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.UnknownAccountException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/Player")
public class PlayerLoginController {

    @Autowired
    IPlayerUserService playerUserService;

    @RequestMapping("/captcha.jpg")
    public void captcha(HttpServletRequest request,ModelMap model,HttpSession session,
                        HttpServletResponse response)throws ServletException, IOException {

        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "10");
        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = kaptcha.createText();
        //生成图片验证码
        BufferedImage image = kaptcha.createImage(text);
        //保存到shiro session
        // ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        session.setAttribute(SessionContants.KAPTCHA_SESSION_KEY,text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    //登陆页面
    @RequestMapping({"/","login","/login.html"})
    public String index(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON user =(JSON)session.getAttribute(SessionContants.PLAYERUSERINFO);
        if (user == null) {
            return "player/login";
        }
        Player1 pUser=JSON.toJavaObject(user,Player1.class);
        model.put("user", pUser);
        model.put("resultStr", "");
        //设置session
        session.setAttribute(SessionContants.PLAYERUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(pUser));
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("playerid",pUser.getId());

        return "player/main";

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request,HttpSession session,ModelMap model){
        try {
            String resultStr="";
            String username=request.getParameter("loginName"); //登录名
            String password=request.getParameter("md5Password");  // 登录密码
            String kaptcha = request.getParameter("kaptcha"); //验证码

            if(StringUtils.isEmpty(kaptcha)){
                resultStr="请输入验证码!";
            }else if(!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY).toString())){
                resultStr="验证码不正确";
                model.put("errorInfo", resultStr);
                return "player/login";
            }
            Player player=new Player();
            player.setUsername(username);
            player.setPassword(password);

            Player1 pUser = playerUserService.getPlayerUser(player);
            if(StringUtils.isEmpty(pUser)){
                resultStr="用户("+username+")不存在!";
            }else if(pUser!=null && !pUser.getPassword().equals(password)){
                resultStr="用户："+username+",密码不正确!";
            }else if(pUser.getStatus()!=1){
                resultStr="用户："+username+"已被限制使用!";
            }
            if(!StringUtils.isBlank(resultStr)){
                request.setAttribute("errorInfo", resultStr);
                return "player/login";
            }

            playerUserService.update(pUser);
            model.put("user", pUser);
            model.put("resultStr", resultStr);
            session.setAttribute(SessionContants.PLAYERUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(pUser));

            return "player/main";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorInfo", "请求异常");
        }
        return "player/login";
    }
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                         @RequestParam Map<String, Object> params) {
        session.removeAttribute(SessionContants.PLAYERUSERINFO);
        if(params.containsValue("refUrl")){
            String refUrl=params.get("refUrl").toString();
            return refUrl;
        }
        return "player/login";
    }
}
