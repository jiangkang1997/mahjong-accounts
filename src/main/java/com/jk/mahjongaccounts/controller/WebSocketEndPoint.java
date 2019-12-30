package com.jk.mahjongaccounts.controller;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.jk.mahjongaccounts.common.*;
import com.jk.mahjongaccounts.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jk
 */
@Slf4j
@ServerEndpoint("/net/websocket/{tableId}/{userId}/{userName}")
@Component
public class WebSocketEndPoint {

    private WebSocketService webSocketService;

    public WebSocketEndPoint() {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
    }


    @OnOpen
    public void onOpen(@PathParam("tableId") String tableId,
                       @PathParam("userId") Integer userId,
                       @PathParam("userName") Integer userName,
                       Session session) {
        log.info("{}加入了{}房间", userId, userName);
        try {
           webSocketService.onOpen(tableId, userId, userName, session);
        } catch (Exception e) {
            try {
                log.error(e.getMessage(), e);
                WebSocketUtil.sendMessage(session, WebsocketResponseBuilder.builderFail("系统错误，连接已断开"));
                session.close();
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("有新消息： {}", message);
        try {
            webSocketService.onMessage(message, session);
        } catch (Exception e) {
            WebSocketUtil.sendMessage(session, WebsocketResponseBuilder.builderFail("系统错误，请重新提交"));
        }
    }

    @OnClose
    public void onClose(@PathParam("tableId") String tableId,
                        @PathParam("userId") Integer userId,
                        @PathParam("userName") Integer userName,Session session) {
        log.info("连接关闭： {}", session);
        try {
            webSocketService.onClose(tableId, userId, userName, session);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError Exception");
    }

}