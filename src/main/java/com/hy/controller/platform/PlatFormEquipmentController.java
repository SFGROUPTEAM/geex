package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.*;
import com.hy.entity.*;

import com.hy.service.IEquipmentPicService;
import com.hy.service.IEquipmentPropertyService;
import com.hy.service.IEquipmentService;
import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 装备管理
 */
@Controller
@RequestMapping("/platformEquipment")
public class PlatFormEquipmentController {
    private Logger log = Logger.getLogger(PlatFormEquipmentController.class);
    @Resource
    private IEquipmentService equipmentService;
    @Resource
    private IEquipmentPicService equipmentPicService;
    @Resource
    private IEquipmentPropertyService equipmentPropertyService;

    @RequestMapping(value = "/showGameEquipmentInfo")
    public String showGameEquipmentInfo(HttpSession session, ModelMap model, String bid) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/equipment/equipmentInfo";
    }

    @RequestMapping(value = "/equipmentFormSave")
    public String equipmentForm(HttpSession session, ModelMap model, String bid) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        Equipment1 gameEquipment = new Equipment1();
        model.put("equipment", gameEquipment);
        return "platform/equipment/equipmentForm";
    }

    @ResponseBody
    @RequestMapping(value = "/equipment_getList")
    public String showEquipmentList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, String equipmentno, String state, String status, String name,
                                    @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //调用service
        Map<String, Object> map = new HashMap<String, Object>();
        String page = (String) params.get("page");
        if (StringUtils.isEmpty(page)) page = "1";
        String pagesize = (String) params.get("limit");
        if (StringUtils.isBlank(pagesize)) pagesize = "10";
        map.put("equipmentno", equipmentno);
        map.put("name", name);
        map.put("state", state);
        map.put("status", status);
        map.put("pagenum", page);
        map.put("pagesize", pagesize);
        List<Equipment> equipment1sList = equipmentService.getSimpleEquipmentList(map);
        Integer listCount = equipmentService.getSimpleEquipmentListCount(map);
        String result = "";
        try {
            result = JsonMapper.toJsonString(equipment1sList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/saveEquipment", method = RequestMethod.POST)
    public String saveUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, Equipment equipment) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        JSONObject jsonObject = JSON.parseObject(operUser.toString());
        String username = (String) jsonObject.get("username");
        if (StringUtils.isEmpty(equipment.getId())) {
            equipment.setId(UUID.randomUUID().toString().replace("-", ""));
            equipment.setCreateuser(username);
            equipment.setCategoryid("6D276467CCFE8DE6E050000ACE000809");
            Integer add = equipmentService.add(equipment);
            if (add == 1) {
                model.put("isFreshFlag", "Y");
                model.put("msg", "新增成功");
                model.put("gameEquipment", equipment);
                return "platform/equipment/equipmentForm";
            } else {
                model.put("isFreshFlag", "Y");
                model.put("msg", "新增失败");
                return "platform/equipment/equipmentForm";
            }
        } else {
//            String[] split = operUser.toJSONString().split("\":\"");
//            System.out.println(operUser.toJSONString());
//            System.out.println(split[split.length-1]);
            equipment.setUpdateuser(username);
            Integer update = equipmentService.update(equipment);
            if (update == 1) {
                model.put("isFreshFlag", "Y");
                model.put("msg", "修改成功");
                model.put("gameEquipment", equipment);
                return "platform/equipment/equipmentForm";
            } else {
                model.put("isFreshFlag", "Y");
                model.put("msg", "修改失败");
                return "platform/equipment/equipmentForm";
            }
        }
    }

    @RequestMapping("/qryEquipment")
    public String showEquipmentInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, Equipment1 gameEquipment1,
                                    @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //调用service
        Equipment1 gameEquipmentInfo = equipmentService.getEquipment1(gameEquipment1);
        model.put("equipment", gameEquipmentInfo);
        return "platform/equipment/equipmentForm";
    }

    @RequestMapping("/qryEquipment1")
    public String qryEquipment1(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, Equipment1 gameEquipment1,
                                @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //调用service
        Equipment1 gameEquipmentInfo = equipmentService.getEquipment1(gameEquipment1);
        model.put("equipment", gameEquipmentInfo);
        return "platform/equipment/equipmentLoadPic";
    }

    @RequestMapping("/qryEquipmentProperty")
    public String qryEquipment2(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, EquipmentProperty1 equipmentProperty1, String name,
                                @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //调用service

        EquipmentProperty1 equipmentProperty = equipmentPropertyService.getEquipmentProperty1(equipmentProperty1);
        if (equipmentProperty == null) {
            equipmentProperty = equipmentProperty1;
            equipmentProperty.setEquipmentname(name);
        }
        model.put("equipmentProperty", equipmentProperty);
        return "platform/equipment/equipmentPublic";
    }

    @ResponseBody
    @RequestMapping("/LoadFile")
    public Map LoadFile(@RequestParam("file") MultipartFile file,
                        HttpServletRequest request, HttpSession session, ModelMap model) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        Map map1 = new HashMap();
        if (puser == null) {
            map1.put("code", "88");
            map1.put("msg", "登录用户信息读取失败，请重新登录");
            return map1;
        }
        MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String equipmentid = multipartRequest.getParameter("id"); // equipmentid info
        String remark = multipartRequest.getParameter("remark"); // remark info
        String showlevel = multipartRequest.getParameter("showlevel"); //showlevel info
        String usetype = multipartRequest.getParameter("usetype"); //usetype info
        String path = null;// 文件路径
        WebDavUtils utils = new WebDavUtils();
        if (!file.isEmpty()) {
            Map map = utils.uploadingPic("equipmentPic/", file);
            if (map != null && map.get("retCode").equals("00")) {
                EquipmentPic gamePic = new EquipmentPic();
                gamePic.setId(UUID.randomUUID().toString().replace("-", ""));
                gamePic.setEquipmentid(equipmentid);
                gamePic.setPicurl(map.get("filePath").toString());
                gamePic.setRemark(remark);
                gamePic.setShowlevel(Integer.parseInt(showlevel));
                gamePic.setUsetype(Integer.parseInt(usetype));
                gamePic.setCreateuser(puser.getUsername());
                gamePic.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
                int i = equipmentPicService.add(gamePic);
                if (i > 0) {
                    map1.put("code", "00");
                    map1.put("msg", "上传成功");
                } else {
                    map1.put("code", "01");
                    map1.put("msg", "上传失败");
                }
            } else {
                map1.put("code", map.get("retCode"));
                map1.put("msg", map.get("retMsg"));
                return map1;
            }
            return map1;
        } else {
            map1.put("code", "99");
            map1.put("msg", "文件不能为空");
            return map1;
        }
    }

    /**
     * 获取装备图片信息
     */
    @ResponseBody
    @RequestMapping(value = "/equipmentpic_List")
    public String showEquipmentPicList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
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
        List<EquipmentPic1> equipmentpicList = equipmentPicService.getList(reqParam);
        Integer listCount = equipmentPicService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(equipmentpicList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取装备发布信息
     */
    @ResponseBody
    @RequestMapping(value = "/equipmentpub_List")
    public String showEquipmentPubList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
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
        List<EquipmentProperty1> equipmentpubList = equipmentPropertyService.getList(reqParam);
        Integer listCount = equipmentPropertyService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(equipmentpubList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Sardine sardine = null;
        try {
            sardine = SardineFactory.begin("davadmin", "123654");
            String path = "http://192.168.40.20/webdav/";
            if (sardine.exists(path)) {
                System.out.println("/content/dam folder exists");
            }
            //     sardine.createDirectory(path+"test1");

            InputStream fis = new FileInputStream("c:\\2.jpg");

            sardine.put(path + "test1/" + "2.jpg", fis);
            List<DavResource> resources = sardine.getResources("http://192.168.40.20/webdav/test1/");
            for (DavResource res : resources) {
                System.out.println(res); // calls the .toString() method.
            }

        } catch (SardineException e) {
            e.printStackTrace();
        }


    }

    @RequestMapping(value = "/saveEquipmentProperty", method = RequestMethod.POST)
    public String saveEquipmentProperty(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, EquipmentProperty1 equipmentProperty) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        JSONObject jsonObject = JSON.parseObject(operUser.toString());
        String username = (String) jsonObject.get("username");
        EquipmentProperty1 equipmentProperty1 = equipmentPropertyService.getEquipmentProperty1(equipmentProperty);
        if(equipmentProperty1==null){
            equipmentProperty.setCreateuser(username);
            Integer add = equipmentPropertyService.add(equipmentProperty);
            if (add == 1) {
                //变更发布状态
                Equipment equipment = new Equipment();
                equipment.setId(equipmentProperty.getEquipmentid());
                equipment.setUpdateuser(username);
                equipment.setOccupyuser(username);
                equipment.setStatus(1);
                equipmentService.update(equipment);
                model.put("isFreshFlag", "Y");
                model.put("equipmentProperty", equipmentProperty);
                return "platform/equipment/equipmentPublic";
            } else {
                return null;
            }
        }else{
            equipmentProperty.setUpdateuser(username);
            Integer update = equipmentPropertyService.update(equipmentProperty);
            if (update == 1) {
                model.put("isFreshFlag", "Y");
                model.put("equipmentProperty", equipmentProperty);
                return "platform/equipment/equipmentPublic";
            } else {
                return null;
            }
        }


    }
}