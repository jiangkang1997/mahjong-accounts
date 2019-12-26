package com.jk.mahjongaccounts.common;

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
