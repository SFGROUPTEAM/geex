package com.hy.controller.api;

import com.hy.common.RedisUtil;
import com.hy.common.StringUtils;
import com.hy.entity.*;
import com.hy.service.IEquipmentLogService;
import com.hy.service.IGoldLogService;
import com.hy.service.player.IPlayerEquipmentService;
import com.hy.service.player.IPlayerGoldService;
import com.hy.service.player.IPlayerUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api")
@Api(value = "用户道具", description = "用户道具接口", produces = MediaType.APPLICATION_JSON)
public class AppUserEquipment {

    private final static Logger log = LoggerFactory.getLogger(AppUserEquipment.class);

    @Autowired
    IPlayerUserService playerUserService;

    @Autowired
    IPlayerEquipmentService playerEquipmentService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/equipment", method = RequestMethod.POST)
    @ApiOperation(value = "我的道具", notes = "我的道具")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId",value = "sessionId", required = true,paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户Id", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagenum",value = "当前页", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagesize",value = "每页大小", required = true,paramType = "query"),
    })
    public Map<String,Object> doPostUserEquipment(HttpServletRequest request,String sessionId,String userId){
        Map<String,Object> resultMap=new TreeMap<String,Object>();
        String requestMap = request.getQueryString();
        String respMsg="";
        log.info("UserEquipment requestMap:"+requestMap);
        if(StringUtils.isEmpty(sessionId)){
            respMsg="未指定sessionId";
        }else if(StringUtils.isEmpty(userId)){
            respMsg="未指定userId";
        }
        if(!StringUtils.isEmpty(respMsg)) {
            resultMap.put("respCode", "01");
            resultMap.put("respMsg", respMsg);
            log.info("UserEquipment resultMap:" + resultMap);
            return resultMap;
        }
        RedisUtil redisUtil=new RedisUtil(redisTemplate);
        Object checkUserOnline=redisTemplate.opsForValue().get(sessionId+userId);
        //检查用户是否在线
        if (StringUtils.isEmpty(checkUserOnline)) {
            resultMap.put("respCode","98");
            resultMap.put("respMsg", "未登陆或者会话超时");
            log.info("UserGold resultMap:"+resultMap);
            return resultMap;
        }else if (StringUtils.isEmpty(userId)) {
            resultMap.put("respCode","03");
            resultMap.put("respMsg", "未指定用户ID");
            log.info("UserGold resultMap:"+resultMap);
            return resultMap;
        }
        try {
            Player1 player1=new Player1();
            player1.setId(userId);
            Player1 pUser = playerUserService.getPlayerUser(player1);

            Map param=request.getParameterMap();
            log.info("param:"+param);

            String pagenum=!StringUtils.isEmpty(param.get("pagenum"))?param.get("pagenum").toString():"1";
            String pagesize=!StringUtils.isEmpty(param.get("pagesize"))?param.get("pagesize").toString():"10";;
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("playerid",pUser.getId());

            map.put("pagenum",pagenum);
            map.put("pagesize",pagesize);
            map.put("data1",param.containsKey("data1")?param.get("data1"):"");
            map.put("data2",param.containsKey("data2")?param.get("data2"):"");

            List<PlayerEquipment1> list=playerEquipmentService.getList(map);
            Integer listCount=playerEquipmentService.getListCount(map);

            resultMap.put("respCode","00");
            resultMap.put("list",list);
            resultMap.put("listCount",listCount);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("UserGold 请求异常"+e.getMessage());
        }
        resultMap.put("respCode","99");
        resultMap.put("respMsg","FAILURE");
        log.info("UserGold resultMap:"+resultMap);
        return resultMap;
    }


    @RequestMapping(value = "/equipment",method = RequestMethod.GET)
    @ApiOperation(value = "我的道具", notes = "我的道具")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId",value = "sessionId", required = true,paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户Id", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagenum",value = "当前页", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagesize",value = "每页大小", required = true,paramType = "query"),
    })
    public Map<String,Object> doGetUserEquipment(HttpServletRequest request,HttpSession session,String sessionId,String userId){
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap=doPostUserEquipment(request,sessionId,userId);
        return resultMap;
    }
}
