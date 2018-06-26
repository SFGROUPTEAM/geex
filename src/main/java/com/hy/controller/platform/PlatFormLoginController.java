package com.hy.controller.platform;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSON;
import com.hy.common.DateUtils;
import com.hy.common.FileUtils;
import com.hy.common.StringUtils;
import com.hy.common.SessionContants;
import com.hy.entity.GameFunctionExport;
import com.hy.entity.Person;
import com.hy.entity.PlatFormMenu;
import com.hy.entity.PlatFormUser;

import com.hy.service.IGameInfoService;
import com.hy.service.platform.IPlatFormMenuService;
import com.hy.service.platform.IPlatFormUserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import net.sf.json.JSONObject;
//import org.apache.shiro.authc.ExcessiveAttemptsException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.UnknownAccountException;

import org.slf4j.Logger;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.commons.io.IOUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by Lijianguo on 2018/4/24.
 * 平台登录控制类
 */
@Controller
@EnableRedisHttpSession
@RequestMapping("/PlatForm")
public class PlatFormLoginController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PlatFormLoginController.class);
    @Resource
    private IPlatFormUserService platFormUserService;
    @Resource
    private IPlatFormMenuService platFormMenuService;

    @Resource
    private IGameInfoService iGameInfoService;
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


    // 处理登陆页面
    @RequestMapping({"/","","/login.html"})
    public String index(HttpServletRequest request,HttpSession session,ModelMap model) {
        JSON user =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (user == null) {
            return "platform/login";
        }
        PlatFormUser pUser=JSON.toJavaObject(user,PlatFormUser.class);
        Map<String,Object> map=new HashMap<String,Object>();
        List<PlatFormMenu> menuList=new ArrayList<PlatFormMenu>();
        if(pUser.getUsertype()==0){ //管理员
            menuList=platFormMenuService.getList(map);
        }else{  //普通操作员
            map.put("userid", pUser.getId());
            menuList=platFormMenuService.getList(map);
        }
        model.put("user", pUser);
        model.put("menuList", menuList);
        model.put("resultStr", "");
        //设置session
        session.setAttribute(SessionContants.PLATFORMUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(pUser));
        return "platform/main";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request,ModelMap model,HttpSession session) {
        try {
            String username=request.getParameter("loginName"); //登录名
            String password=request.getParameter("md5Password");  // 登录密码
            String kaptcha=request.getParameter("kaptcha");  // 验证码
            //String application = request.getParameter("application"); // 应用类型
            String resultStr="";
            if(StringUtils.isEmpty(kaptcha)){
                resultStr="请输入验证码!";
            }else if(!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY))){
                resultStr="验证码输入错误!";
            }
            if(!StringUtils.isEmpty(resultStr)){
                model.put("resultStr", resultStr);
                request.setAttribute("errorInfo", resultStr);
                return "platform/login";
            }
            PlatFormUser loginUser=new PlatFormUser();
            loginUser.setUsername(username);
            //loginUser.setPassword(password);
            PlatFormUser pUser = platFormUserService.getPlatFormUser(loginUser);
            if(pUser==null){
                resultStr="用户("+username+")不存在!";
            }else if(!(pUser.getPassword().toUpperCase()).equals(password.toUpperCase())){
                resultStr="用户："+username+",密码不正确!";
            }else if(pUser.getState()==0){
                resultStr="用户："+username+"已经被禁用!";
            }
            if(!StringUtils.isEmpty(resultStr)){
                model.put("resultStr", resultStr);
                request.setAttribute("errorInfo", resultStr);
                return "platform/login";
            }

            Map<String,Object> map=new HashMap<String,Object>();
            List<PlatFormMenu> menuList=new ArrayList<PlatFormMenu>();
           if(pUser.getUsertype()==0){ //管理员
                menuList=platFormMenuService.getList(map);
           }else{  //普通操作员
               map.put("userid", pUser.getId());
               menuList=platFormMenuService.getUserList(map);
           }
            model.put("user", pUser);
            model.put("menuList", menuList);
            model.put("resultStr", "");
            //System.out.println("pUserInfo111"+session.getAttribute(SessionContants.PLATFORMUSERINFO));
            //if(StringUtil.isEmpty(session.getAttribute("p_"+pUser.getUsername()))){
            //设置session
            session.setAttribute(SessionContants.PLATFORMUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(pUser));
            //System.out.println("pUserInfo222"+session.getAttribute(SessionContants.PLATFORMUSERINFO));
            return "platform/main";
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorInfo", "请求异常");
        }
        return "platform/login";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                         @RequestParam Map<String, Object> params) {
        session.removeAttribute(SessionContants.PLATFORMUSERINFO);
        return "platform/login";
    }


}
