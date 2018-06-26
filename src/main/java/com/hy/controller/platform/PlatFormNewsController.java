package com.hy.controller.platform;
import com.alibaba.fastjson.JSON;
import com.hy.common.*;
import com.hy.entity.News;
import com.hy.entity.PlatFormUser;
import com.hy.service.INewsService;
import com.hy.service.INewsService;
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

import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

@Controller
@RequestMapping("/PNews")
public class PlatFormNewsController {
    private Logger log = Logger.getLogger(PlatFormNewsController.class);
    @Resource
    private INewsService newsService;

    @ResponseBody
    @RequestMapping("/news_render_list")
    public String getNewsrenderList(HttpServletRequest request, HttpSession session, ModelMap model) {

        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        log.info("news_render_list -- start");
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

        List<News> newsList = newsService.getList(reqParam);
        Integer listCount = newsService.getListCount(reqParam);
        String result = "";
        try {
            result = JsonMapper.toJsonString(newsList);
            result = "{\"code\":" + "0" + "," + "\"count\":" + listCount + "," + "\"page\":" + page + "," + "\"limit\":" + pagesize + "," + "\"data\":" + result + "}";
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/getNewsList")
    public String getNewsList(HttpServletRequest request,HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/news/newsManage";
    }

    /**
     * 公告/新闻 信息（新增、修改、查看）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/form")
    public String form(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        Map<String, Object> reqParam = ApplicationBase.getAllRequestParam(request);
        News news = new News();
        Object obj = reqParam.get("id");
        if (obj == null) {
            reqParam.put("id", "");
        }
        if (!StringUtils.isEmpty(reqParam.get("id").toString())) {
            news.setId(reqParam.get("id").toString());
            news = newsService.getNews(news);
        }
        model.put("news", news);
        return "platform/news/newsForm";
    }

    /**
     * 公告/新闻 信息保存提交（新增、修改）
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/news/save", method = RequestMethod.POST)
    public String addNews(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,News news) {
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

        if (StringUtils.isBlank(news.getId())) {
//            if (listCount > 0) {
//                model.put("isFreshFlag", "N");
//                model.put("news", news);
//                model.put("msg", "已存在该条公告信息.");
//                return "platform/news/newsForm";
//            }
            news.setId(UUID.randomUUID().toString().replace("-", ""));
            news.setCreatetime(DateUtils.getDisplayYMDHHMMSS());
            news.setCreateuser(puser.getUsername());
            newsService.add(news);
        } else {
            news.setUpdatetime(DateUtils.getDisplayYMDHHMMSS());
            news.setUpdateuser(puser.getUsername());
            newsService.update(news);
        }

        model.put("isFreshFlag", "Y");
        model.put("news", news);
        return "platform/news/newsForm";
    }

    @ResponseBody
    @RequestMapping(value = "/news/audit")
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
                return "缺失必要参数：公告/新闻ID不能为空。";
            }
            if (StringUtils.isEmpty(reqParam.get("status"))) {
                return "缺失必要参数：审核状态不能为空。";
            }
            News news = new News();
            news.setId(reqParam.get("id").toString());
            news = newsService.getNews(news);
            news.setUpdateuser(puser.getUsername());
            news.setStatus(Integer.parseInt(reqParam.get("status").toString()));
            newsService.update(news);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAILURE";
        }
    }

}
