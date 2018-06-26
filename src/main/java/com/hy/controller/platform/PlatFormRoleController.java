package com.hy.controller.platform;
import com.alibaba.fastjson.JSON;
import com.hy.common.DateUtils;
import com.hy.common.*;
import com.hy.entity.*;
import com.hy.service.platform.IPlatFormRoleButtonService;
import com.hy.service.platform.IPlatFormRoleMenuService;
import com.hy.service.platform.IPlatFormRoleService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
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

@Controller
@RequestMapping("/PRole")
public class PlatFormRoleController {

    private Logger log = Logger.getLogger(PlatFormRoleController.class);
    @Resource
    private IPlatFormRoleService platFormRoleService;
    @Resource
    private IPlatFormRoleMenuService platFormRoleMenuService;
    @Resource
    private IPlatFormRoleButtonService platFormRoleButtonService;

    @ResponseBody
    @RequestMapping("/role_render_list")
    public String getRoleList(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        log.info("role_render_list -- start");
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

        List<PlatFormRole> rolesList = platFormRoleService.getList(reqParam);
        Integer listCount = platFormRoleService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(rolesList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/getRoleList")
    public String getRoleList1(HttpServletRequest request,HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/role/RoleManage";
    }

    @RequestMapping(value = "/role/form")
    public String form(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        PlatFormRole role = new PlatFormRole();
        Object obj = reqParam.get("roleid");
        if (obj == null) {
            reqParam.put("roleid", "");
        }
        if (!StringUtils.isEmpty(reqParam.get("roleid").toString())) {
            role.setId(reqParam.get("roleid").toString());
            role = platFormRoleService.getPlatFormRole(role);
        }
        model.put("role", role);
        return "platform/role/roleForm";
    }

    @RequestMapping(value = "/role/save", method = RequestMethod.POST)
    public String addRole(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,PlatFormRole role) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser user = JSON.parseObject(jsonString, PlatFormUser.class);
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);

        Map<String, Object> vmap = new HashMap<String, Object>();
        vmap.put("name1",role.getName());
        Integer listCount = platFormRoleService.getListCount(vmap);

        if (StringUtils.isBlank(role.getId())) {
            if (listCount > 0) {
                model.put("isFreshFlag", "N");
                model.put("role", role);
                model.put("msg", "已存在该角色信息.");
                return "platform/role/roleForm";
            }
            role.setState(1);
            role.setId(UUID.randomUUID().toString().replace("-", ""));
            role.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
            role.setCreateuser(user.getUsername());
            platFormRoleService.add(role);
        } else {
            platFormRoleService.update(role);
        }
        // 角色菜单
        if(!StringUtils.isEmpty(reqParam.get("selectMenuId"))){
            PlatFormRoleMenu roleMenu=new PlatFormRoleMenu();
            roleMenu.setRoleid(role.getId());
            platFormRoleMenuService.delete(roleMenu);
            String menuIds[]=reqParam.get("selectMenuId").toString().split(",");
            if(menuIds!=null&&menuIds.length>=1){
                for(int i=0;i<menuIds.length;i++){
                    if(StringUtils.isNotEmpty(menuIds[i])){
                        roleMenu=new PlatFormRoleMenu();
                        roleMenu.setRoleid(role.getId());
                        roleMenu.setMenuid(menuIds[i]);
                        platFormRoleMenuService.add(roleMenu);
                    }
                }
            }

        }
        //角色按钮
        /*if(!StringUtils.isEmpty(reqParam.get("selectButtonId"))){
            String sBut = reqParam.get("selectButtonId").toString();
            JSONObject json = JSONObject.fromObject(sBut);
            if(!json.isNullObject()){
                PlatFormRoleButton roleButton=new PlatFormRoleButton();
                roleButton.setRoleid(role.getId());
                platFormRoleButtonService.delete(roleButton);

                Iterator iterator = json.keys();
                String key="";
                String value="";
                while(iterator.hasNext()){
                    PlatFormRoleButton  roleButton1=new PlatFormRoleButton();
                    roleButton1.setRoleid(role.getId());
                    key = (String) iterator.next();
                    value = json.getString(key);
                    roleButton1.setButtonid(key);
                    roleButton1.setMenuid(value);
                    platFormRoleButtonService.add(roleButton1);
                }
            }
        }*/





        model.put("isFreshFlag", "Y");
        model.put("role", role);
        return "platform/role/roleForm";


    }

    @ResponseBody
    @RequestMapping(value = "/role/upt")
    public String delete(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        try {
            JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
            if (operUser == null) {
                model.put("isLoginOut", "true");
                return "platform/login";
            }
            String jsonString = JSON.toJSONString(operUser);
            PlatFormUser user = JSON.parseObject(jsonString, PlatFormUser.class);
            Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
            if (StringUtils.isEmpty(reqParam.get("roleid"))) {
                return "缺失必要参数：角色ID不能为空。";
            }
            if (StringUtils.isEmpty(reqParam.get("state"))) {
                return "缺失必要参数：状态不能为空。";
            }
            PlatFormRole role = new PlatFormRole();
            role.setId(reqParam.get("roleid").toString());
            role = platFormRoleService.getPlatFormRole(role);
            role.setState(Integer.parseInt(reqParam.get("state").toString()));
            role.setUpdateuser(user.getUsername());
            role.setUpdatetime(DateUtils.getDisplayYMDHHMMSS());
            platFormRoleService.update(role);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }
    @RequestMapping("/qryUserRole")
    public String qryUserRole(HttpServletRequest request,HttpSession session, ModelMap model,String userid) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        if (StringUtils.isEmpty(userid)) {
            userid="0";
        }
        model.put("userid",userid);
        return "platform/role/qryUserRoleList";
    }

    @ResponseBody
    @RequestMapping("/role_user_list")
    public String getUserRoleList(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        log.info("role_user_list -- start");
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
        List<PlatFormRole1> rolesList = platFormRoleService.qryUserRoleList(reqParam);
        Integer listCount = platFormRoleService.getListCount1(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(rolesList);
            result=result.replace("lay_CHECKED","LAY_CHECKED");
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
