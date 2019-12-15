package com.jk.mahjongaccounts.controller;

import java.io.IOException;
import java.util.Date;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



/**
 * @author jk
 */
@Slf4j
@Component
@ServerEndpoint("/net/websocket/{key}/{name}")//表明这是一个websocket服务的端点
public class WebSocketEndPoint {

    private Integer count = 0;


    @OnOpen
    public void onOpen(@PathParam("key") String key, @PathParam("name") String name,  Session session){
        log.info("有新的连接：{}", session);
        WebSocketHandler.sendMessage(session, "服务器给你发送了一条消息"+new Date().getTime());
        log.info("在线人数：{}",++count);
    }

    @OnMessage
    public void onMessage(String message){
        log.info("有新消息： {}", message);
    }

    @OnClose
    public void onClose(@PathParam("key") String key, @PathParam("name") String name,Session session){
        log.info("连接关闭： {}", session);
        log.info("在线人数：{}",--count);
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