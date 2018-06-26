package com.hy.controller.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hy.common.SessionContants;
import com.hy.entity.GameCategory;
import com.hy.entity.GameCategory1;
import com.hy.service.IGameCategoryService;
import com.hy.service.IGameCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/platformGameCategory")
public class PlatFormGameCategoryController {
    @Resource
    private IGameCategoryService gameCategoryService;
    @RequestMapping("/showGameCategoryInfo")
    public String showGameInfo(HttpSession session, ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/gameCategory/gameCategory";
    }
    @RequestMapping("/chooseParent")
    public String chooseParent(HttpSession session, ModelMap model){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        return "platform/gameCategory/list";
    }
    @RequestMapping("/form")
    public String showGameInfoForm(HttpSession session, ModelMap model,@RequestParam Map<String, Object> params, String Id,String isFather){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        GameCategory temp = new GameCategory();
        temp.setId(Id);
        GameCategory gameCategory = gameCategoryService.getGameCategory(temp);
        temp.setId(gameCategory.getParentid());
        GameCategory gameCategoryP = gameCategoryService.getGameCategory(temp);
        model.put("gameCategory",gameCategory);
        if(gameCategoryP!=null){
            model.put("parentname",gameCategoryP.getName());
        }else{
            model.put("parentname","无");
        }
        if("Y".equals(isFather)){
            model.put("isFather","该分类存在子类，无法对启用禁用进行操作");
        }
        return "platform/gameCategory/form";
    }
    @ResponseBody
    @RequestMapping("/gCategoryTree1")
    public String doList1(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model,
                          @RequestParam Map<String, Object> params, String roleid) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<GameCategory1> menuList = gameCategoryService.getList(map);
        List<GameCategory1> nodelist = GameCategory1.buildByRecursive(menuList);
        String nodeInfo = JSONObject.toJSONString(nodelist);
        nodeInfo = nodeInfo.replace("data", "children").replace("id", "value");
        return nodeInfo;
    }
    @RequestMapping("/saveCategory")
    public String saveCategory(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, GameCategory gameCategory){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        JSONObject jsonObject = JSONObject.parseObject(operUser.toString());
        String username = (String) jsonObject.get("username");
        gameCategory.setId(UUID.randomUUID().toString().replace("-", ""));
        gameCategory.setCreateuser(username);
        Integer add = gameCategoryService.add(gameCategory);
        if(add==1){
            model.put("add","新增成功");
        }else{
            model.put("add","新增失败");
        }
        return "platform/gameCategory/gameCategory";
    }
    @RequestMapping("/updateCategory")
    public String updateCategory(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model, GameCategory gameCategory,String parentname,String isFather){
        JSON operUser =(JSON)session.getAttribute(SessionContants.PLATFORMUSERINFO);
        if (operUser == null) {
            model.put("isLoginOut", "true");
            return "platform/login";
        }
        //验证parentid是否是自己
        if (gameCategory.getId().equals(gameCategory.getParentid())){
            model.put("isFreshFlag","Y");
            model.put("msg","无法选择自己作为父分类");
            model.put("parentname",parentname);
            model.put("gameCategory",gameCategory);
            return "platform/gameCategory/form";
        }
        //验证parentid是否是自己的子类
        Map<String, Object> map = new HashMap<String, Object>();
        List<GameCategory1> menuList =gameCategoryService.getList(map);
        List<GameCategory1> nodelist = GameCategory1.buildByRecursive(menuList);
        for (GameCategory1 gameCategory1 : nodelist) {
            if (gameCategory1.getId().equals(gameCategory.getId())){
                List<GameCategory1> data = gameCategory1.getData();
                for (GameCategory1 datum : data) {
                    if (gameCategory.getParentid().equals(datum.getId())){
                        model.put("isFreshFlag","Y");
                        model.put("msg","无法选择自己的子类作为父分类");
                        model.put("parentname",parentname);
                        model.put("gameCategory",gameCategory);
                        return "platform/gameCategory/form";
                    }
                }
            }
        }

        JSONObject jsonObject = JSONObject.parseObject(operUser.toString());
        String username = (String) jsonObject.get("username");
        gameCategory.setUpdateuser(username);
        Integer update = gameCategoryService.update(gameCategory);
        if(update==1){
            model.put("msg","更新成功");
        }else{
            model.put("msg","更新失败");
        }
        model.put("isFreshFlag","Y");
        model.put("gameCategory",gameCategory);
        model.put("parentname",parentname);
        return "platform/gameCategory/form";
    }
}
