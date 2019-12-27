package com.jk.mahjongaccounts.common;

import com.jk.mahjongaccounts.model.RelateTableSession;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 */
@Slf4j
public class WebSocketHandler {

    private static RedisTemplateMapper redisTemplateMapper;

    static {
        redisTemplateMapper = SpringUtil.getBean(RedisTemplateMapper.class);
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
            log.error("sendText Exception:", e);
        }
    }

    /**
     * 群发消息
     * @param sessionIds
     * @param message
     */
    public static void sendMessage(List<String> sessionIds,String message){
        if(sessionIds.size() != 0){
            for (String sessionId : sessionIds) {
                Session session = SessionMap.get(sessionId);
                if(session != null){
                    sendMessage(session,message);
                }
            }
        }
    }

    /**
     * 给桌内所有人  群发消息
     * @param tableId
     * @param message
     */
    public static void sendMessageForTable(String tableId,String message) throws Exception {
        RelateTableSession relateTableSession = redisTemplateMapper.getByTableId(RedisKey.TABLE_SESSION_HASH, tableId, RelateTableSession.class);
        if(relateTableSession != null){
            List<String> sessionIds = relateTableSession.getSessionIds();
            if(sessionIds != null && sessionIds.size() != 0){
                sendMessage(sessionIds,message);
            }
        }
    }

}
