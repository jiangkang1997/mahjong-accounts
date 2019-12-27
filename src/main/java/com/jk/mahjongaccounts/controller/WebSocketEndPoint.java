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

import com.jk.mahjongaccounts.common.*;
import com.jk.mahjongaccounts.model.RelateTableSession;
import com.jk.mahjongaccounts.model.RelateTableUser;
import com.jk.mahjongaccounts.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jk
 */
@Slf4j
@ServerEndpoint("/net/websocket/{tableId}/{userId}/{userName}")
@Component
public class WebSocketEndPoint {

    private RedisTemplateMapper redisTemplateMapper;

    public WebSocketEndPoint() {
        redisTemplateMapper = SpringUtil.getBean(RedisTemplateMapper.class);
        //userMapper = SpringUtil.getBean(UserMapper.class);
    }


    @OnOpen
    public void onOpen(@PathParam("tableId") String tableId,
                       @PathParam("userId") Integer userId,
                       @PathParam("userName") Integer userName,
                       Session session) {
        log.info("{}加入了{}房间", userId, userName);
        RelateTableSession relateTableSession;
        List<String> sessionIds;
        try {
            relateTableSession = redisTemplateMapper.getByTableId(RedisKey.TABLE_SESSION_HASH, tableId, RelateTableSession.class);
            if (relateTableSession == null) {
                relateTableSession = new RelateTableSession(tableId);
                sessionIds = new ArrayList<>();
            } else {
                sessionIds = relateTableSession.getSessionIds();
                if (sessionIds.size() >= 4) {
                    WebSocketHandler.sendMessage(session, "该房间已满 三秒后断开链接");
                    Thread.sleep(3000);
                    session.close();
                    return;
                }
                WebSocketHandler.sendMessage(sessionIds, "玩家" + userName + "加入房间");
            }
            sessionIds.add(session.getId());
            relateTableSession.setSessionIds(sessionIds);
            //存入桌与session关系  用于分组群发消息
            redisTemplateMapper.push(RedisKey.TABLE_SESSION_HASH, tableId, relateTableSession);
            //存放session实体，由于session无法序列化，因此单独存放
            SessionMap.put(session.getId(), session);
            //存放 用户与桌的关系 用于掉线重连
            redisTemplateMapper.set(String.valueOf(userId), tableId);
            WebSocketHandler.sendMessage(session, "您已成功链接房间");
        } catch (Exception e) {
            try {
                WebSocketHandler.sendMessage(session, "系统错误");
                session.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("有新消息： {}", message);
    }

    @OnClose
    public void onClose(@PathParam("tableId") String tableId, Session session){
        //异常退出，删除 sessionMap，删除桌与session关系，
        // 保留桌与用户关系，可重连
        log.info("连接关闭： {}", session);
        try {
            session.close();
            SessionMap.remove(session.getId());
            RelateTableSession relateTableSession = redisTemplateMapper.getByTableId(RedisKey.TABLE_SESSION_HASH, tableId, RelateTableSession.class);
            if(relateTableSession != null){
                List<String> sessionIds = relateTableSession.getSessionIds();
                if(sessionIds != null){
                    sessionIds.remove(session.getId());
                    //如果四人都断开了连接，则删除所有人的桌与用户关系（解散该桌）
                    if(sessionIds.size() == 0){
                        RelateTableUser relateTableUser = redisTemplateMapper.getByTableId(RedisKey.TABLE_USER_HASH,tableId, RelateTableUser.class);
                        List<User> users = relateTableUser.getUsers();
                        if(users != null && users.size()!=0){
                            for (User user : users) {
                                redisTemplateMapper.del(String.valueOf(user.getUserId()));
                            }
                        }
                        redisTemplateMapper.del(RedisKey.TABLE_USER_HASH,tableId);
                    }
                }
                redisTemplateMapper.push(RedisKey.TABLE_SESSION_HASH,tableId,relateTableSession);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("onError Exception");
    }

}