package com.jk.mahjongaccounts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jk.mahjongaccounts.common.RedisKey;
import com.jk.mahjongaccounts.common.RedisTemplateMapper;
import com.jk.mahjongaccounts.common.WebSocketHandler;
import com.jk.mahjongaccounts.model.RelateTableSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @author jk
 */
@Slf4j
@Component
@ServerEndpoint("/net/websocket/{tableId}/{userId}")
public class WebSocketEndPoint {

    @Autowired
    RedisTemplateMapper redisTemplateMapper;


    @OnOpen
    public void onOpen(@PathParam("tableId") String tableId, @PathParam("userId") String userId,  Session session){
        log.info("{}加入了{}房间",userId,tableId);
        RelateTableSession relateTableSession;
        try {
            relateTableSession = redisTemplateMapper.getByTableId(RedisKey.TABLE_SESSION_HASH, tableId, RelateTableSession.class);
            if(relateTableSession  == null){
                relateTableSession = new RelateTableSession(tableId);
            }
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            relateTableSession.setSessions(sessions);
            redisTemplateMapper.push(RedisKey.TABLE_SESSION_HASH,tableId,relateTableSession);
        } catch (Exception e) {
            WebSocketHandler.sendMessage(session,"系统错误");
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("有新消息： {}", message);
        System.out.println(session);
    }

    @OnClose
    public void onClose(@PathParam("key") String key, @PathParam("name") String name,Session session){
        log.info("连接关闭： {}", session);
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        try {
            session.close();
        } catch (IOException e) {
            log.error("onError Exception: {}", e);
        }
        log.info("连接出现异常： {}", throwable);
    }

}