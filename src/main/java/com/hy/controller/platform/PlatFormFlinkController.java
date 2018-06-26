package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.hy.common.*;
import com.hy.entity.FriendLink;
import com.hy.entity.PlatFormUser;
import com.hy.service.IFriendLinkService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/PFlink")
public class PlatFormFlinkController {
    private Logger log = Logger.getLogger(PlatFormFlinkController.class);
    @Resource
    private IFriendLinkService friendLinkService;

    @ResponseBody
    @RequestMapping("/flink_render_list")
    public String getFlinkrenderList(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        log.info("flink_render_list -- start");
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

        List<FriendLink> flinkList = friendLinkService.getList(reqParam);
        Integer listCount = friendLinkService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(flinkList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getFlinkList")
    public String getNewsList(HttpServletRequest request,HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/friendlink/friendlinkManage";
    }

    /**
     * 友情链接 信息（新增、修改、查看）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/flink/form")
    public String form(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        FriendLink friendLink = new FriendLink();
        Object obj = reqParam.get("id");
        if (obj == null) {
            reqParam.put("id", "");
        }
        if (!StringUtils.isEmpty(reqParam.get("id").toString())) {
            friendLink.setId(reqParam.get("id").toString());
            friendLink = friendLinkService.getFriendLink(friendLink);
        }
        model.put("flink", friendLink);
        return "platform/friendlink/friendlinkForm";
    }

    /**
     * 公告/新闻 信息保存提交（新增、修改）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/flink/save", method = RequestMethod.POST)
    public String addNews(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,FriendLink friendLink) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        String jsonString = JSON.toJSONString(operUser);
        PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);

        Map<String, Object> vmap = new HashMap<String, Object>();
 //   vmap.put("username1",user.getUsername());
 //   Integer listCount = platFormUserService.getListCount(vmap);

        if (StringUtils.isBlank(friendLink.getId())) {
//            if (listCount > 0) {
//                model.put("isFreshFlag", "N");
//                model.put("news", news);
//                model.put("msg", "已存在该条公告信息.");
//                return "platform/news/newsForm";
//            }
            friendLink.setId(UUID.randomUUID().toString().replace("-", ""));
            friendLink.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
            friendLink.setCreateuser(puser.getUsername());
            friendLink.setStatus(0);
            friendLinkService.add(friendLink);
        } else {
            friendLink.setUpdatetime(DateUtils.getDisplayYMDHHMMSS());
            friendLink.setUpdateuser(puser.getUsername());
            friendLinkService.update(friendLink);
        }

        model.put("isFreshFlag", "Y");
        model.put("flink", friendLink);
        return "platform/friendlink/friendlinkForm";
    }

    @ResponseBody
    @RequestMapping(value = "/flink/audit")
    public String audit(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        try {
            JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
            if (operUser == null) {
                model.put("isLoginOut", "true");
                return "platform/login";
            }
            String jsonString = JSON.toJSONString(operUser);
            PlatFormUser puser = JSON.parseObject(jsonString, PlatFormUser.class);
            Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
            if (StringUtils.isEmpty(reqParam.get("id"))) {
                return "缺失必要参数：友情链接ID不能为空。";
            }
            if (StringUtils.isEmpty(reqParam.get("status"))) {
                return "缺失必要参数：审核状态不能为空。";
            }
            FriendLink friendLink = new FriendLink();
            friendLink.setId(reqParam.get("id").toString());
            friendLink = friendLinkService.getFriendLink(friendLink);
            friendLink.setUpdateuser(puser.getUsername());
            friendLink.setStatus(Integer.parseInt(reqParam.get("status").toString()));
            friendLinkService.update(friendLink);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

}
