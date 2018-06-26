package com.hy.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hy.common.*;
import com.hy.entity.*;
import com.hy.service.api.IApiPlayerGoldService;
import com.hy.service.api.IPlayerLoginLengthService;
import com.hy.service.api.IPlayerRechargeService;
import com.hy.service.platform.IGameFunctionService;
import com.hy.service.player.IPlayerUserService;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class ApiController {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(ApiController.class);
    @Resource
    IGameFunctionService gameFunctionService;

    @Resource
    IPlayerUserService playerUserService;

    @Resource
    IPlayerLoginLengthService playerLoginLengthService;

    @Resource
    IApiPlayerGoldService playerGoldService;
    @Resource
    IPlayerRechargeService playerRechargeService;

    /**
     * 返回用户名、密码、上次登录时长
     * 根据时长计算金币
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/auth")
    public String userAuthorize(HttpServletRequest request) {
        String errorStr = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        String requestStr = null;
        try {
            requestStr = ApplicationBase.getRequestString(request);
        } catch (IOException e) {

        }
        log.info("call /api/auth,receive:"+requestStr);
        if (StringUtils.isEmpty(requestStr)) {
            errorStr="无请求数据";
            resultMap.put("respCode", "96");
            resultMap.put("respMsg",errorStr );
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        Map<String, String> requestMap = null;
        try {
            requestMap = JSON.parseObject(requestStr, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            errorStr="请求格式非法，请确保是JSON格式";
            resultMap.put("respCode", "97");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (!requestMap.containsKey("signature")) {
            errorStr = "参数错误，未指定签名（signature）";
        } else if (!requestMap.containsKey("gameNo")) {
            errorStr = "参数错误，未指定游戏编码（gameNo）";
        } else if (!requestMap.containsKey("username")) {
            errorStr = "参数错误，未指定游戏账号（username）";
        } else if (!requestMap.containsKey("password")) {
            errorStr = "参数错误，未指定游戏密码（password）";
        } else if (!requestMap.containsKey("loginLength")) {
            errorStr = "参数错误，未指定游戏登录时长（loginLength）";
        } else if (!requestMap.containsKey("voucherNo")) {
            errorStr = "参数错误，未指定登录凭证号（voucherNo）";
        }//登录凭证号+gameNo
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "01");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        String username = requestMap.get("username");
        String password = requestMap.get("password");
        String gameNo = requestMap.get("gameNo");
        String loginLength = requestMap.get("loginLength");
        String voucherNo = requestMap.get("voucherNo");
        String fromSignature = requestMap.get("signature");
        //-----开始验证签名-----------
        String fromVerifyString = ApplicationBase.coverMap2String(requestMap);//根据map获取的签名前字符串
        GameFunction1 gameFunction1 = null;
        try {
            gameFunction1 = gameFunctionService.getGameFunctionByGameNo(gameNo);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (gameFunction1 == null) {
            errorStr = "无法找到游戏相关内容";
        } else if ("0".equals(gameFunction1.getGamestatus())) {
            errorStr = "游戏审核未通过";
        } else if ("0".equals(gameFunction1.getGamestate())) {
            errorStr = "游戏已被限制使用";
        }
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "02");
            resultMap.put("respMsg", errorStr);

            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        try {
            //验签
            if (!RSAUtil.verify(fromVerifyString.getBytes("UTF-8"), gameFunction1.getG_publickey(), fromSignature)) {
                errorStr = "签名验证失败";
            }
        } catch (Exception e) {
            errorStr = "验签出现异常";
        }
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "A0");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        //---验证签名结束-------------------
        //------------------验证登录凭证号开始-----------------
        //根据用户名、凭证查找登录记录
        Map<String, Object> voucherNoMap = new HashMap<>();
        voucherNoMap.put("gameno", gameNo);
        voucherNoMap.put("voucherno", voucherNo);
        PlayerLoginLength playerLoginLength1 = null;
        try {
            playerLoginLength1 = playerLoginLengthService.getPlayerLoginLengthByGameNoAndVoucherNo(voucherNoMap);
        } catch (Exception e) {
            errorStr = "系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (playerLoginLength1 != null) {
            errorStr = "登录凭证号失效";
            resultMap.put("respCode", "03");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        //------------------凭证号验证结束------------------
        Player player = new Player();
        player.setUsername(username);
        Player pUser = null;
        try {
            pUser = playerUserService.getPlayerUser(player);
        } catch (Exception e) {
            errorStr = "系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (pUser == null) {
            errorStr="用户不存在";
            resultMap.put("respMsg", errorStr);
            resultMap.put("respCode", "04");
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        //---密码判断---
        String origPassword = "";
        try {
            byte[] decPassword = RSAUtil.decryptBASE64(password);
            origPassword = new String(RSAUtil.decryptByPrivateKey(decPassword, gameFunction1.getP_privatekey()));
        } catch (Exception e) {

        }
        String md5Password = MD5Utils.MD5(origPassword);
        if (!pUser.getPassword().toUpperCase().equals(md5Password.toUpperCase())) {
            errorStr="用户名密码错误";
            resultMap.put("respMsg", errorStr);
            resultMap.put("respCode", "05");
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (pUser.getStatus() != 1) {
            errorStr="该用户已被限制使用";
            resultMap.put("respMsg", errorStr);
            resultMap.put("respCode", "06");
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }

        //插表
        PlayerLoginLength playerLoginLength = new PlayerLoginLength();
        playerLoginLength.setGameno(gameNo);
        playerLoginLength.setLoginlength(Integer.parseInt(loginLength));
        playerLoginLength.setPlayerid(pUser.getId());
        playerLoginLength.setVoucherno(voucherNo);
        String id = UUID.randomUUID().toString().replace("-", "");
        playerLoginLength.setId(id);


        //验证成功--2.调用存储过程计算GOLD
        Map<String, Object> map = new HashMap<>();
        map.put("t_type", "1");
        map.put("t_id", id);
        try {
            playerLoginLengthService.add(playerLoginLength);
            playerGoldService.calculateGold(map);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        resultMap.put("respMsg", "账户合法");
        resultMap.put("respCode", "00");
        //验证成功--1.加入参数
        resultMap.put("username", pUser.getUsername());
        resultMap.put("nickname", pUser.getNickname());
        resultMap.put("gendar", pUser.getGendar().toString());
        resultMap.put("phone", pUser.getPhone());
        resultMap.put("birthday", pUser.getBirthday());
        resultMap.put("email", pUser.getEmail());
        resultMap.put("lastVisitTime", pUser.getLastvisittime());
        resultMap.put("QQ", pUser.getQq());
        resultMap.put("remark", pUser.getRemark());
        resultMap.put("lastVisitIP", pUser.getLastvisitip());
        resultMap.put("registerIP", pUser.getRegisterip());
        resultMap.put("registerTime", pUser.getRegistsertime());
        resultMap.put("skype", pUser.getSkype());
        //加入签名
        String toVerifyString = ApplicationBase.coverMap2String(resultMap);
        try {
            resultMap.put("signature", RSAUtil.sign(toVerifyString.getBytes("UTF-8"), gameFunction1.getP_privatekey()));
        } catch (Exception e) {
            errorStr="签名构建失败";
            resultMap.put("respCode", "A1");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/auth:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        log.info("call /api/auth,response:"+JSON.toJSONString(resultMap));
        return JSON.toJSONString(resultMap);
    }

    /**
     * 返回充值成功后的充值金额
     * 根据金额等条件计算金额
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/rechargeNotify")
    public String rechargeResult(HttpServletRequest request) {
        String errorStr = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        String requestStr = null;
        try {
            requestStr = ApplicationBase.getRequestString(request);
        } catch (IOException e) {
            requestStr="";
        }
        log.info("call /api/rechargeNotify,receive:"+requestStr);
        if (StringUtils.isEmpty(requestStr)) {
            errorStr="无请求数据";
            resultMap.put("respCode", "96");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        Map<String, String> requestMap = null;
        try {
            requestMap = JSON.parseObject(requestStr, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            errorStr="请求格式非法，请确保是JSON格式";
            resultMap.put("respCode", "97");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (!requestMap.containsKey("signature")) {
            errorStr = "参数错误，未指定签名（signature）";
        } else if (!requestMap.containsKey("gameNo")) {
            errorStr = "参数错误，未指定游戏编码（gameNo）";
        } else if (!requestMap.containsKey("username")) {
            errorStr = "参数错误，未指定游戏账号（username）";
        } else if (!requestMap.containsKey("amount")) {
            errorStr = "参数错误，未指定游戏充值金额（amount）";
        } else if (!NumberUtil.isNumber(requestMap.get("amount"))) {
            errorStr = "参数错误，游戏充值金额参数非法";
        } else if (!requestMap.containsKey("orderNo")) {
            errorStr = "参数错误，未指定充值订单号（orderNo）";
        }//充值订单号+gameNo
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "01");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        String username = requestMap.get("username");
        String amount = requestMap.get("amount");
        String gameNo = requestMap.get("gameNo");
        String fromSignature = requestMap.get("signature");
        String currency = requestMap.get("currency");
        String orderNo = requestMap.get("orderNo");

//-----开始验证签名-----------
        String fromVerifyString = ApplicationBase.coverMap2String(requestMap);//根据map获取的签名前字符串
        GameFunction1 gameFunction1 = null;
        try {
            gameFunction1 = gameFunctionService.getGameFunctionByGameNo(gameNo);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (gameFunction1 == null) {
            errorStr = "无法找到游戏相关内容";
        } else if ("0".equals(gameFunction1.getGamestatus())) {
            errorStr = "游戏审核未通过";
        } else if ("0".equals(gameFunction1.getGamestate())) {
            errorStr = "游戏已被限制使用";
        }
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "02");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        try {
            //验签
            if (!RSAUtil.verify(fromVerifyString.getBytes("UTF-8"), gameFunction1.getG_publickey(), fromSignature)) {
                errorStr = "签名验证失败";
            }
        } catch (Exception e) {
            errorStr = "验签出现异常";
        }
        if (!StringUtils.isEmpty(errorStr)) {
            resultMap.put("respCode", "A0");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }

        //------------验签结束----------
//----------------验证订单号开始----------------
        //根据用户名、凭证查找登录记录
        Map<String, Object> orderNoMap = new HashMap<>();
        orderNoMap.put("gameno", gameNo);
        orderNoMap.put("orderno", orderNo);
        PlayerRecharge playerRecharge1 = null;
        try {
            playerRecharge1 = playerRechargeService.getPlayerRechargeByGameNoAndOrderNo(orderNoMap);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (playerRecharge1 != null) {
            errorStr = "订单号已处理完成,无法再次处理";
            resultMap.put("respCode", "03");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
//----------------验证订单号结束----------------
//---------获取用户------------
        Player player = new Player();
        player.setUsername(username);
        Player playerUser = null;
        try {
            playerUser = playerUserService.getPlayerUser(player);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        if (playerUser == null) {
            errorStr="用户不存在";
            resultMap.put("respCode", "04");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }

        //插表
        PlayerRecharge playerRecharge = new PlayerRecharge();
        String id = UUID.randomUUID().toString().replace("-", "");
        playerRecharge.setId(id);
        playerRecharge.setPlayerid(playerUser.getId());
        playerRecharge.setAmount(Integer.parseInt(amount));
        playerRecharge.setCurrency(currency);
        playerRecharge.setOrderno(orderNo);
        playerRecharge.setGameno(gameNo);
//                        游戏参数等待定
//                        playerRecharge.setParam1();
//                        playerRecharge.setParam2();
//                        playerRecharge.setParam3();
//                        playerRecharge.setParam4();

        //计算GOLD
        Map<String, Object> map = new HashMap<>();
        map.put("t_type", "2");
        map.put("t_id", id);
        try {
            playerRechargeService.add(playerRecharge);
            playerGoldService.calculateGold(map);
        } catch (Exception e) {
            errorStr="系统异常";
            resultMap.put("respCode", "98");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        resultMap.put("respCode", "00");
        resultMap.put("respMsg", "信息已收到");
        //加入签名
        String toVerifyString = ApplicationBase.coverMap2String(resultMap);
        try {
            resultMap.put("signature", RSAUtil.sign(toVerifyString.getBytes("UTF-8"), gameFunction1.getP_privatekey()));
        } catch (Exception e) {
            errorStr="签名构建失败";
            resultMap.put("respCode", "A1");
            resultMap.put("respMsg", errorStr);
            log.error("call /api/rechargeNotify:"+errorStr);
            return JSON.toJSONString(resultMap);
        }
        log.info("call /api/rechargeNotify,response:"+JSON.toJSONString(resultMap));
        return JSON.toJSONString(resultMap);
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        String G_privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK4qsCPjEcvNMWYBaYumf3Bn6o3ykcdOgx9GxuVugjlU6qNnhCHopYYii8D1nPGRKfFRYv24BNPwDEVhMICAGREFxHLTAHXnL/oSNNxFrLa/x36LnWwoRktEV0H+gkD/3XmQwdkiNNxskMdj2AN58oqq/qSHhIzW+6nhekmpSnOfAgMBAAECgYBXAJgQtUOUjSIaFKaGzltm6WIwGnZcG4pAu3hr7/I5dYPosLM+xRq6kNNDRu5pddx/4eoTWDOBM67NkalK57KdChMoRvmnhMWS5v0XZYnSUPMuhLS8mbcg6+4Mibyfo4Cn9/m/jFNEimS7b7mRQJNEI9L4rwRXXWAYjAjg/RzMAQJBAPuJSFAGSMm+oqOpx4QHwZzuzZidc8ITtP5NjntOjVAF7pnHFI5J7grNF8QPv3NVXYqJcA1hsmAuPZ2CJ/pgbQECQQCxQetj6wiUBvMl6hI/zBaZJWv/EEmcU9DJopppv2muPpQr37sxkj/diiZ6uZKgwEPrnpkpwfb+DjOabYsnpsCfAkAvOkJyjAuEnZ+J3SnR12b54TiHDCEc8nhMOyGic0hDGyoGg4OPh8ADFSj+LfrDW+bZSR7Z1FsahfQZq8N31j0BAkEAh7+iE71Vrne8Epc2LqGP1O0xzf6dQRl3VlQuyTmMJ6NJOmh3JbmJK5K3cVlZD77uxS2Kws4G5/3cSzIFVfKApQJAFnwVpu5Y6/lg7SKUnMNiCnwftT2LiQo20oCaXVNDZ4NbfH+4MLa81AluLU4MiRIl2ZEcOpW38Uw7lS4XH5D+4w==";
        String P_publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJTL3Rc8cy3h4mTMME9zpG8yXpQwC6AAlcv2Xyp/84+Vmid4Hl7acX1f0I/5qqvrgas84yLMAn0NjRzkR5MrM80VlEfqG+9qLJOmYnxeMl+0aMM4WM9+gKexeHKJmlxhTIjuAFYcwGf5ST4q1P6LZoKNnFrqitgLuy7juwEp0NPQIDAQAB";
        String G_publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuKrAj4xHLzTFmAWmLpn9wZ+qN8pHHToMfRsblboI5VOqjZ4Qh6KWGIovA9ZzxkSnxUWL9uATT8AxFYTCAgBkRBcRy0wB15y/6EjTcRay2v8d+i51sKEZLRFdB/oJA/915kMHZIjTcbJDHY9gDefKKqv6kh4SM1vup4XpJqUpznwIDAQAB";

        map.put("username", "jinxiaoyu");
 //       String password = RSAUtil.encryptBASE64(RSAUtil.encryptByPublicKey("111111".getBytes("UTF-8"), P_publicKey));
//        map.put("password", password);
        map.put("gameNo", "NO2222222");
//        map.put("loginLength", "600");
        map.put("amount", "500");
        map.put("orderNo", "0000000000000000");
//        map.put("voucherNo", "0000000000013300");
        String srcString = ApplicationBase.coverMap2String(map);
        String signature = RSAUtil.sign(srcString.getBytes("UTF-8"), G_privateKey);
        map.put("signature", signature);
        System.out.println(JSON.toJSONString(map));
        String json = JSON.toJSONString(map);
        Map<String, String> jsonMap = (Map<String, String>) JSON.parse(json);
        System.out.println("****************************************************");
        System.out.println(RSAUtil.verify(srcString.getBytes("UTF-8"), G_publicKey, jsonMap.get("signature")));

    }
}
