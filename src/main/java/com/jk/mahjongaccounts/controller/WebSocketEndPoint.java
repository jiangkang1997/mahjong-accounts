package com.jk.mahjongaccounts.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jk.mahjongaccounts.common.*;
import com.jk.mahjongaccounts.model.RelateTableSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jk
 */
@Slf4j
@ServerEndpoint("/net/websocket/{tableId}/{userId}")
@Component
public class WebSocketEndPoint {

    private Map<String,RelateTableSession> sessionMap;

    public WebSocketEndPoint() {
        sessionMap = new ConcurrentHashMap<>();
    }


    @OnOpen
    public void onOpen(@PathParam("tableId") String tableId, @PathParam("userId") String userId, Session session){
        log.info("{}加入了{}房间",userId,tableId);
        RelateTableSession relateTableSession;
        try {
            relateTableSession = sessionMap.get(tableId);
            if(relateTableSession  == null){
                relateTableSession = new RelateTableSession(tableId);
                System.out.println(session.getId());
            }
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            relateTableSession.setSessions(sessions);
            sessionMap.put(tableId,relateTableSession);
        } catch (Exception e) {
            try {
                WebSocketHandler.sendMessage(session,"系统错误");
                session.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            log.error(e.getMessage(),e);
        }
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("有新消息： {}", message);
    }

    @OnClose
    public void onClose(@PathParam("key") String key, @PathParam("name") String name, Session session){
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