package com.hy.controller.api;

import com.hy.common.RedisUtil;
import com.hy.common.StringUtils;
import com.hy.entity.GoldLog;
import com.hy.entity.Player1;
import com.hy.entity.PlayerGold;
import com.hy.service.IGoldLogService;
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
@Api(value = "用户金币", description = "用户金币接口", produces = MediaType.APPLICATION_JSON)
public class AppUserGold {

    private final static Logger log = LoggerFactory.getLogger(AppUserGold.class);

    @Autowired
    IPlayerUserService playerUserService;

    @Autowired
    IPlayerGoldService playerGoldService;

    @Autowired
    IGoldLogService goldLogService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping(value = "/gold", method = RequestMethod.POST)
    @ApiOperation(value = "我的金币", notes = "我的金币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId",value = "sessionId", required = true,paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户Id", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagenum",value = "当前页", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagesize",value = "每页大小", required = true,paramType = "query")
    })
    public Map<String,Object> doPostUserGold(HttpServletRequest request,String sessionId,String userId){
        Map<String,Object> resultMap=new TreeMap<String,Object>();
        String requestMap = request.getQueryString();
        log.info("UserGold requestMap:"+requestMap);
        String respMsg="";
        if(StringUtils.isEmpty(sessionId)){
            respMsg="未指定sessionId";
        }else if(StringUtils.isEmpty(userId)){
            respMsg="未指定userId";
        }
        if(!StringUtils.isEmpty(respMsg)) {
            resultMap.put("respCode", "01");
            resultMap.put("respMsg", respMsg);
            log.info("UserGold resultMap:" + resultMap);
            return resultMap;
        }
        RedisUtil redisUtil=new RedisUtil(redisTemplate);
        Object checkUserOnline=redisTemplate.opsForValue().get(sessionId+userId);
        //检查用户是否在线
        if (StringUtils.isEmpty(checkUserOnline)) {
            resultMap.put("respCode","98");
            resultMap.put("respMsg", "未登陆或者会话超时.");
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
            PlayerGold playerGold=new PlayerGold();
            playerGold.setPlayerid(pUser.getId());
            playerGold=playerGoldService.getPlayerGold(playerGold);
            if(playerGold!=null) {
                BigDecimal cnt = new BigDecimal(playerGold.getGoldcnt());
                DecimalFormat df = new DecimalFormat(",###,##0.00"); //保留一位小数
                String goldcnt = String.valueOf(df.format(cnt));
                playerGold.setGoldcnt(goldcnt);
            }else{
                playerGold=new PlayerGold();
                playerGold.setPlayerid(pUser.getId());
                BigDecimal cnt = new BigDecimal("0");
                DecimalFormat df = new DecimalFormat(",###,##0.00"); //保留一位小数
                String goldcnt = String.valueOf(df.format(cnt));
                playerGold.setGoldcnt(goldcnt);
            }

            String pagenum=!StringUtils.isEmpty(param.get("pagenum"))?param.get("pagenum").toString():"1";
            String pagesize=!StringUtils.isEmpty(param.get("pagesize"))?param.get("pagesize").toString():"10";;
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("playerid",pUser.getId());

            map.put("pagenum",pagenum);
            map.put("pagesize",pagesize);
            map.put("data1",param.containsKey("data1")?param.get("data1"):"");
            map.put("data2",param.containsKey("data2")?param.get("data2"):"");

            List<GoldLog> goldLogList=goldLogService.getList(map);
            Integer listCount=goldLogService.getListCount(map);

            resultMap.put("respCode","00");
            resultMap.put("goldInfo",playerGold);
            resultMap.put("goldLogList",goldLogList);
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


    @RequestMapping(value = "/gold",method = RequestMethod.GET)
    @ApiOperation(value = "我的金币", notes = "我的金币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sessionId",value = "sessionId", required = true,paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户Id", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagenum",value = "当前页", required = true,paramType = "query"),
            @ApiImplicitParam(name = "pagesize",value = "每页大小", required = true,paramType = "query")
    })
    public Map<String,Object> doGetUserGold(HttpServletRequest request,HttpSession session,String sessionId,String userId){
        Map<String,Object> resultMap=new HashMap<String,Object>();
        resultMap=doPostUserGold(request,sessionId,userId);
        return resultMap;
    }
}
