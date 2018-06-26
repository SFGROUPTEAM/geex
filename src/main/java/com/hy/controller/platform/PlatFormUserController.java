package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.hy.common.*;
import com.hy.entity.PlatFormUser;
import com.hy.entity.PlatFormUserRole;
import com.hy.service.platform.IPlatFormUserRoleService;
import com.hy.service.platform.IPlatFormUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Created by Lijianguo on 2018/4/24.
 * 平台用户控制类
 */
@Controller
@RequestMapping("/PUser")
public class PlatFormUserController {
    private Logger log = Logger.getLogger(PlatFormUserController.class);
    @Resource
    private IPlatFormUserService platFormUserService;
    @Resource
    private IPlatFormUserRoleService platFormUserRoleService;

    @Resource
    private LocaleMessageSourceService localeMessageSourceService;
    @ResponseBody
    @RequestMapping("/user_render_list")
    public String getUserList(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "PlatForm/";
        }
        log.info("user_render_list -- start");
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

        List<PlatFormUser> userList = platFormUserService.getList(reqParam);
        Integer listCount = platFormUserService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(userList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getUserList")
    public String userList(HttpServletRequest request,HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/user/UserManage";
    }

    /**
     * 操作员信息（新增、修改、查看）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/form")
    public String form(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        PlatFormUser user = new PlatFormUser();
        Object obj = reqParam.get("id");
        if (obj == null) {
            reqParam.put("id", "");
        }
        if (!StringUtils.isEmpty(reqParam.get("id").toString())) {
            user.setId(reqParam.get("id").toString());
            user = platFormUserService.getPlatFormUser(user);
        }
        model.put("user", user);
        return "platform/user/userForm";
    }

    /**
     * 操作员信息保存提交（新增、修改）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public String addUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,PlatFormUser user) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);

        Map<String, Object> vmap = new HashMap<String, Object>();
        vmap.put("username1",user.getUsername());
        Integer listCount = platFormUserService.getListCount(vmap);

        if (StringUtils.isBlank(user.getId())) {
            if (listCount > 0) {
                model.put("isFreshFlag", "N");
                model.put("user", user);
                model.put("msg", "已存在该用户名.");
                return "platform/user/userForm";
            }
            user.setId(UUID.randomUUID().toString().replace("-", ""));
            user.setPassword(MD5Utils.MD5(user.getPassword()));
            user.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
            user.setCreateuser(puser.getUsername());
            platFormUserService.add(user);
        } else {
            user.setUpdatetime(DateUtils.getDisplayYMDHHMMSS());
            user.setUpdateuser(puser.getUsername());
            platFormUserService.update(user);
        }

        // 角色菜单
        if(!StringUtils.isEmpty(reqParam.get("roleidstr"))){
            PlatFormUserRole userRole=new PlatFormUserRole();
            userRole.setUserid(user.getId());
            platFormUserRoleService.delete(userRole);
            String roleidstr[]=reqParam.get("roleidstr").toString().split(",");
            if(roleidstr!=null&&roleidstr.length>=1){
                for(int i=0;i<roleidstr.length;i++){
                    if(StringUtils.isNotEmpty(roleidstr[i])){
                        userRole=new PlatFormUserRole();
                        userRole.setUserid(user.getId());
                        userRole.setRoleid(roleidstr[i]);
                        platFormUserRoleService.add(userRole);
                    }
                }
            }
        }
        model.put("isFreshFlag", "Y");
        model.put("user", user);
        return "platform/user/userForm";
    }

    @ResponseBody
    @RequestMapping(value = "/user/upt")
    public String delete(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        try {
            JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
            if (operUser == null) {
                model.put("isLoginOut", "true");
                return "platform/login";
            }
            Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
            if (StringUtils.isEmpty(reqParam.get("id"))) {
                return "缺失必要参数：操作员ID不能为空。";
            }
            if (StringUtils.isEmpty(reqParam.get("state"))) {
                return "缺失必要参数：状态不能为空。";
            }
            PlatFormUser user = new PlatFormUser();
            user.setId(reqParam.get("id").toString());
            user = platFormUserService.getPlatFormUser(user);
            user.setState(Integer.parseInt(reqParam.get("state").toString()));
            platFormUserService.update(user);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

    /**
     * 修改密码页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/modifyPwd")
    public String modifyLoginPwd(HttpSession session, ModelMap model,String type){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        model.put("user",puser);
        model.put("type",type);
        return "platform/user/modifyPwd";
    }

    @ResponseBody
    @RequestMapping(value = "/checkPwd")
    public String checkPwd(String oldPassword,ModelMap model, String type,HttpSession session) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        PlatFormUser puser1 =platFormUserService.getPlatFormUser(puser);
        if(type.equals("login")){//校验登陆原密码
            oldPassword=oldPassword.replaceAll(",","");
            oldPassword= MD5Utils.MD5(oldPassword);
            if (oldPassword !=null && oldPassword.toUpperCase().equals(puser1.getPassword().toUpperCase())) {
                return "true";
            }
        }
        return "false";
    }


    /**
     * 提交修改密码
     */
    @RequestMapping(value={"/modify_pwd/save"}, method = RequestMethod.POST)
    public String saveModifyLoginPwd(HttpSession session, ModelMap model,String newPassword,String type){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        PlatFormUser puser1 =platFormUserService.getPlatFormUser(puser);

        puser1.setPassword(MD5Utils.MD5(newPassword));
        platFormUserService.updatePwd(puser1);
        session.setAttribute(com.hy.common.SessionContants.PLATFORMUSERINFO, com.alibaba.fastjson.JSONObject.toJSON(puser1));
        model.put("user",puser1);
        model.put("type",type);
        model.put("isFreshFlag", "Y");
        return "platform/user/modifyPwd";
    }



    @RequestMapping("/ceshi")
    public String ceshi() {
   //     JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
//        if (operUser == null) {
//            model.put("isLoginOut", "true");
//            return "platform/login";
//        }
//        model.put("ceshi",localeMessageSourceService.getMessage("ceshi"));
//        System.out.println(localeMessageSourceService.getMessage("ceshi"));
        return "ceshi";
    }



}
