package com.hy.controller.api;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.hy.common.ApplicationBase;
import com.hy.common.RedisUtil;
import com.hy.common.SessionContants;
import com.hy.common.StringUtils;
import com.hy.entity.ExUser;
import com.hy.entity.Player;
import com.hy.entity.Player1;
import com.hy.listener.AsyncTask;
import com.hy.service.player.IPlayerUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
@Api(value = "登陆/注册", description = "登陆/注册接口", produces = MediaType.APPLICATION_JSON)
public class AppUser {

    private final static Logger log = LoggerFactory.getLogger(AppUser.class);

    @Autowired
    IPlayerUserService playerUserService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private AsyncTask asyncTask;

    @RequestMapping("/captcha.jpg")
    public Map<String,String> captcha(HttpServletRequest request,ModelMap model,HttpSession session,
                        HttpServletResponse response)throws ServletException, IOException {
        Map<String,String> resultMap=new HashMap<String,String>();

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
        resultMap.put("text",text);
        return resultMap;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登陆", notes = "登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName",value = "登录名", required = true,paramType = "query"),
            @ApiImplicitParam(name = "md5Password",value = "登录密码", required = true,paramType = "query")/*,
            @ApiImplicitParam(name = "kaptcha",value = "验证码", required = true,paramType = "query")*/
    })
    public Map<String,Object> doPostLogin(HttpServletRequest request,HttpSession session){
        Map<String,Object> resultMap=new TreeMap<String,Object>();
        try {
            String requestMap = request.getQueryString();
            log.info("Login requestMap:"+requestMap);
            String respMsg="";
            String username=request.getParameter("loginName"); //登录名
            String password=request.getParameter("md5Password");  // 登录密码
            String kaptcha = request.getParameter("kaptcha"); //验证码

            if(StringUtils.isEmpty(username)){
                respMsg="请输入登录名!";
            }/*else if(StringUtils.isEmpty(kaptcha)){
                respMsg="请输入验证码!";
            }*//*else if(!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY).toString())){
                respMsg="验证码不正确";
            }*/
            if(!StringUtils.isBlank(respMsg)){
                resultMap.put("respCode","01");
                resultMap.put("respMsg",respMsg);
                return resultMap;
            }
            Player player=new Player();
            player.setUsername(username);
            player.setPassword(password);

            Player1 pUser = playerUserService.getPlayerUser(player);
            if(StringUtils.isEmpty(pUser)){
                respMsg="用户("+username+")不存在!";
            }else if(pUser!=null && !pUser.getPassword().equals(password)){
                respMsg="用户："+username+",密码不正确!";
            }else if(pUser.getStatus()!=1){
                respMsg="用户："+username+"已被限制使用!";
            }
            if(!StringUtils.isBlank(respMsg)){
                resultMap.put("respCode","02");
                resultMap.put("respMsg",respMsg);
                return resultMap;
            }

            playerUserService.update(pUser);
            String sessionId=session.getId().replace("-","");
            session.setAttribute("sessionId", sessionId);
            String UserSessionInfo=sessionId+pUser.getId();
            RedisUtil redisUtil=new RedisUtil(redisTemplate);
            boolean ret=redisUtil.set(UserSessionInfo,UserSessionInfo,60*60);
//            System.out.println("ret:"+ret);
            resultMap.put("respCode","00");
            resultMap.put("respMsg","SUCCESS");
            resultMap.put("sessionId",sessionId);
            resultMap.put("user",pUser);
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("UserLogin 请求异常"+e.getMessage());
        }
        resultMap.put("respCode","99");
        resultMap.put("respMsg","FAILURE");
        return resultMap;
    }


    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ApiOperation(value = "登陆", notes = "登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName",value = "登录名", required = true,paramType = "query"),
            @ApiImplicitParam(name = "md5Password",value = "登录密码", required = true,paramType = "query")/*,
            @ApiImplicitParam(name = "kaptcha",value = "验证码", required = true,paramType = "query")*/
    })
    public Map<String,Object> doGetLogin(HttpServletRequest request,HttpSession session){
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap=doPostLogin(request,session);
        return resultMap;
    }




    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName",value = "登录名", required = true,paramType = "query"),
            @ApiImplicitParam(name = "md5Password",value = "登录密码", required = true,paramType = "query")/*,
            @ApiImplicitParam(name = "kaptcha",value = "验证码", required = true,paramType = "query")*/
    })
    public Map<String,Object> doPostRegister(HttpServletRequest request,HttpSession session){
        Map<String,Object> resultMap=new TreeMap<String,Object>();
        String respMsg="";
        String requestMap = request.getQueryString();
        log.info("Register requestMap:"+requestMap);
        try {
            String username=request.getParameter("loginName");
            String password=request.getParameter("md5Password");
            String kaptcha = request.getParameter("kaptcha");//验证码
            if(StringUtils.isBlank(username)){
                respMsg="用户名不能为空";
            }else if(StringUtils.isBlank(password)){
                respMsg="密码不能为空";
            }/*else if(!kaptcha.equals(session.getAttribute(SessionContants.KAPTCHA_SESSION_KEY).toString())){
                respMsg="验证码不正确";
            }*/

            if(!StringUtils.isBlank(respMsg)){
                resultMap.put("respCode","04");
                resultMap.put("respMsg",respMsg);
                return resultMap;
            }
            Player tplayer=new Player();
            tplayer.setUsername(username.replace(",",""));
            tplayer = playerUserService.getPlayerUser(tplayer);

            ExUser exUser=new ExUser();
            exUser.setUsername(username.replace(",",""));
            exUser = playerUserService.getExUser(exUser);
            if(!StringUtils.isEmpty(tplayer) || !StringUtils.isEmpty(exUser)){
                resultMap.put("respCode","05");
                resultMap.put("respMsg","该用户已被注册");
                return resultMap;
            }
            String registerip= ApplicationBase.getClientIP(request);
            Player player = new Player();
            player.setRegisterip(registerip);
            player.setUsername(username);
            player.setPassword(password);
            playerUserService.add(player);

            resultMap.put("respCode","00");
            resultMap.put("respMsg","SUCCESS");

            Player pUser = playerUserService.getPlayerUser(player);
            //asyncTask.task1(pUser);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("UserLogin 请求异常"+e.getMessage());
        }
        resultMap.put("respCode","99");
        resultMap.put("respMsg","FAILURE");
        return resultMap;
    }


    @RequestMapping(value = "/register",method = RequestMethod.GET)
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName",value = "登录名", required = true,paramType = "query"),
            @ApiImplicitParam(name = "md5Password",value = "登录密码", required = true,paramType = "query")/*,
            @ApiImplicitParam(name = "kaptcha",value = "验证码", required = true,paramType = "query")*/
    })
    public Map<String,Object> doGetRegister(HttpServletRequest request,HttpSession session){
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap=doPostRegister(request,session);
        return resultMap;
    }
}
