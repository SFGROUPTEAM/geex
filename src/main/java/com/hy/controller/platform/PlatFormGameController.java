package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.*;
import com.hy.dao.IGameInfoDao;
import com.hy.entity.*;
import com.hy.service.IGameInfoService;
import com.hy.service.IGameInfoService;
import com.hy.service.IGamePicService;
import com.hy.service.platform.IGameFunctionService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/platformGame")
public class PlatFormGameController {
    @Resource
    private IGameInfoService gameInfoService;
    @Resource
    private IGamePicService gamePicService;
    @Resource
    private IGameFunctionService gameFunctionService;
    @RequestMapping("/showGameInfo")
    public String showGameInfo(HttpSession session, ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/game/gameInfo";
    }
    @RequestMapping("/gameForm")
    public String gameForm(HttpSession session, ModelMap model,GameInfo1 gameInfo1){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        if (gameInfo1!=null&&gameInfo1.getId()!=null){
            gameInfo1 = gameInfoService.getGameInfo(gameInfo1);
        }else {
            gameInfo1 = new GameInfo1();
        }
        model.put("game",gameInfo1);
        return "platform/game/gameForm";
    }
    @ResponseBody
    @RequestMapping(value = "/game_render_list")
    public String showGameList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,GameInfo1 gameInfo1,
                               @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
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
        map.put("category",gameInfo1.getCategory());
        map.put("gameno",gameInfo1.getGameno());
        map.put("company",gameInfo1.getCompany());
        map.put("name",gameInfo1.getName());
        map.put("state",gameInfo1.getState());
        map.put("status",gameInfo1.getStatus());
        map.put("pagenum", page);
        map.put("pagesize", pagesize);
        List<GameInfo1> gameList = gameInfoService.getList1(map);
        Integer listCount = gameInfoService.getListCount1(map);
        String result = "";
        try {
            result = JsonMapper.toJsonString(gameList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Transactional
    @ResponseBody
    @RequestMapping("/auditGame")
    public String gameAudit(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,GameInfo gameInfo,
                            @RequestParam Map<String, Object> params){
        //验证登录
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        if (StringUtils.isEmpty(reqParam.get("id"))) {
            return "缺失必要参数：游戏ID不能为空。";
        }
        if (StringUtils.isEmpty(reqParam.get("status"))) {
            return "缺失必要参数：审核状态不能为空。";
        }
        GameInfo gameInfo1= new GameInfo();
        gameInfo1.setId(reqParam.get("id").toString());
        gameInfo1=gameInfoService.getGameInfo(gameInfo1);
        gameInfo1.setUpdateuser(puser.getUsername());
        gameInfo1.setStatus(Integer.parseInt(reqParam.get("status").toString()));
        int i=gameInfoService.update(gameInfo1);
        if(i>0){
            try {
                Map<String, Object> mapRsa=RSAUtil.initKey();
                String publicKey=RSAUtil.getPublicKey(mapRsa);
                String privateKey=RSAUtil.getPrivateKey(mapRsa);

                Map<String, Object> mapRsa1=RSAUtil.initKey();
                String publicKey1=RSAUtil.getPublicKey(mapRsa1);
                String privateKey1=RSAUtil.getPrivateKey(mapRsa1);
                GameFunction gf = new GameFunction();
                gf.setG_privatekey(privateKey);
                gf.setG_publickey(publicKey);
                gf.setP_privatekey(privateKey1);
                gf.setP_publickey(publicKey1);
                gf.setGameid(reqParam.get("id").toString());
                gf.setIsenable("1");
                gf.setFunction("0");
                gameFunctionService.add(gf);
                return "SUCCESS";
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "审核失败";
    }
    @ResponseBody
    @RequestMapping(value = "/gamepic_List")
    public String showGamePicList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
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
        List<GamePic1> gamePic1List = gamePicService.getList(reqParam);
        Integer listCount = gamePicService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(gamePic1List);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping("/qryGame")
    public String qryEquipment1(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, GameInfo gameInfo,
                                @RequestParam Map<String, Object> params) {
        //验证登录
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //调用service
        GameInfo gameInfo1 = gameInfoService.getGameInfo(gameInfo);
        model.put("gameInfo", gameInfo1);
        return "platform/game/gameLoadPic";
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
        String gameid = multipartRequest.getParameter("id"); // equipmentid info
        String remark = multipartRequest.getParameter("remark"); // remark info
        String showlevel = multipartRequest.getParameter("showlevel"); //showlevel info
        String usetype = multipartRequest.getParameter("usetype"); //usetype info
        String path = null;// 文件路径
        WebDavUtils utils = new WebDavUtils();
        if (!file.isEmpty()) {
            Map map = utils.uploadingPic("gamePic/", file);
            if (map != null && map.get("retCode").equals("00")) {
                GamePic gamePic = new GamePic();
                gamePic.setId(UUID.randomUUID().toString().replace("-", ""));
                gamePic.setGameid(gameid);
                gamePic.setPicurl(map.get("filePath").toString());
                gamePic.setRemark(remark);
                gamePic.setShowlevel(showlevel);
                gamePic.setUsetype(usetype);
                gamePic.setCreateuser(puser.getUsername());
                gamePic.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
                int i = gamePicService.add(gamePic);
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
    @RequestMapping(value = "/saveGame", method = RequestMethod.POST)
    public String saveGame(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, GameInfo gameInfo) {
        JSON operUser = (JSON) session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        JSONObject jsonObject = JSON.parseObject(operUser.toString());
        String username = (String) jsonObject.get("username");
        if (StringUtils.isEmpty(gameInfo.getId())) {
            gameInfo.setId(UUID.randomUUID().toString().replace("-", ""));
            gameInfo.setCreateuser(username);
            Integer add = gameInfoService.add(gameInfo);
            if (add == 1) {
                model.put("isFreshFlag", "Y");
                model.put("msg", "新增成功");
                GameInfo1 gameInfo1 = new GameInfo1();
                model.put("game", gameInfo1);
                return "platform/game/gameForm";
            } else {
                model.put("isFreshFlag", "Y");
                model.put("msg", "新增失败");
                return "platform/game/gameForm";
            }
        } else {
//            String[] split = operUser.toJSONString().split("\":\"");
//            System.out.println(operUser.toJSONString());
//            System.out.println(split[split.length-1]);
            gameInfo.setUpdateuser(username);
            Integer update = gameInfoService.update(gameInfo);
            if (update == 1) {
                model.put("isFreshFlag", "Y");
                model.put("msg", "修改成功");
                GameInfo1 gameInfo1 = new GameInfo1();
                model.put("game", gameInfo1);
                return "platform/game/gameForm";
            } else {
                model.put("isFreshFlag", "Y");
                model.put("msg", "修改失败");
                return "platform/game/gameForm";
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/gameFunc_List")
    public String showGameFuncList(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
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
        List<GameFunction1> gameFunList = gameFunctionService.getList(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(gameFunList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + gameFunList.size() + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model){
        //验证登录
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);

        Map map = new HashMap();
        map.put("gameid",reqParam.get("gameid"));
        List<GameFunctionExport> list =gameInfoService.getFunctionList(map);
        //导出操作
        FileUtils.exportExcel(list,"对接秘钥信息","sheet",GameFunctionExport.class,"游戏对接参数.xls",response);
       // return "SUCCESS";
    }

}
