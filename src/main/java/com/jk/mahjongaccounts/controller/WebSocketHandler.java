package com.jk.mahjongaccounts.controller;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @author jk
 */
@Slf4j
public class WebSocketHandler {


    /**
     * 根据key和用户名生成一个key值，简单实现下
     *
     * @param key
     * @param name
     * @return
     */
    public static String createKey(String key, String name) {
        return key + "@" + name;
    }

    /**
     * 给指定用户发送信息
     *
     * @param session
     * @param msg
     */
    public static void sendMessage(Session session, String msg) {
        if (session == null){
            return;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null){
            return;
        }
        try {
            basic.sendText(msg);
        } catch (IOException e) {
            log.error("sendText Exception: {}", e);
        }
    }

}
