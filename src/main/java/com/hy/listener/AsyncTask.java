package com.hy.listener;

import com.hy.common.RSAUtil;
import com.hy.entity.ExAsset;
import com.hy.entity.ExUser;
import com.hy.entity.Player;
import com.hy.service.player.IPlayerUserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Future;

@Component
public class AsyncTask {

    @Autowired
    IPlayerUserService playerUserService;

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(AsyncTask.class);

    @Async
    public Future<String> task1(Player pUser) throws InterruptedException{
        if(pUser!=null){
            try {
                Map<String, Object> mapRsa = RSAUtil.initKey();
                String publicKey = RSAUtil.getPublicKey(mapRsa);
                String privateKey = RSAUtil.getPrivateKey(mapRsa);

                //同步交易所用户
                ExUser exUser = new ExUser();
                exUser.setId(pUser.getId());
                exUser.setUsername(pUser.getUsername());
                exUser.setPassword(pUser.getPassword());
                exUser.setPrivatekey(privateKey);
                exUser.setPublickey(publicKey);
                playerUserService.addExUser(exUser);
                //初始化交易所VIT币资金表
                ExAsset exAsset = new ExAsset();
                exAsset.setUserid(pUser.getId());
                playerUserService.addExAsset(exAsset);
            }catch(Exception e){
                log.info("同步交易所用户出错"+e.getMessage());
            }
        }

        return new AsyncResult<String>("task1执行完毕");
    }

}
