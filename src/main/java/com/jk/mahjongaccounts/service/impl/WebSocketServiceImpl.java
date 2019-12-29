package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.*;
import com.jk.mahjongaccounts.mapper.RedisTemplateMapper;
import com.jk.mahjongaccounts.model.RelateTableSession;
import com.jk.mahjongaccounts.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final RedisTemplateMapper redisTemplateMapper;

    @Autowired
    public WebSocketServiceImpl(RedisTemplateMapper redisTemplateMapper) {
        this.redisTemplateMapper = redisTemplateMapper;
    }

    @Override
    public void onOpen(String tableId, Integer userId, Integer userName, Session session) throws Exception {
        RelateTableSession relateTableSession;
        List<String> sessionIds;
        relateTableSession = redisTemplateMapper.getRelateTableSession(tableId);
        if (relateTableSession == null) {
            relateTableSession = new RelateTableSession(tableId);
            sessionIds = new ArrayList<>();
        } else {
            sessionIds = relateTableSession.getSessionIds();
            if (sessionIds.size() >= 4) {
                WebSocketUtil.sendMessage(session, WebsocketResponseBuilder.builderFail("该房间已满 三秒后断开链接"));
                Thread.sleep(3000);
                session.close();
                return;
            }
            WebSocketUtil.sendMessage(sessionIds, WebsocketResponseBuilder.builderSuccess("玩家" + userName + "加入了游戏"));
        }
        sessionIds.add(session.getId());
        relateTableSession.setSessionIds(sessionIds);
        //存入桌与session关系  用于分组群发消息
        redisTemplateMapper.setRelateTableSession(relateTableSession);
        //存放session实体，由于session无法序列化，因此单独存放
        SessionMap.put(session.getId(), session);
        //存放 用户与桌的关系 用于掉线重连
        redisTemplateMapper.setUserTable(String.valueOf(userId), tableId);
        WebSocketUtil.sendMessage(session, WebsocketResponseBuilder.builderSuccess("您已成功链接房间"));
    }

    @Override
    public void onMessage(String message, Session session) throws Exception {

    }

    @Override
    public void onClose(String tableId, Integer userId, Integer userName, Session session) throws Exception {
        session.close();
        //异常退出，删除 sessionMap，删除桌与session关系，
        // 保留桌与用户关系，使其处于重连状态
        SessionMap.remove(session.getId());
        RelateTableSession relateTableSession = redisTemplateMapper.getRelateTableSession(tableId);
        if (relateTableSession != null) {
            List<String> sessionIds = relateTableSession.getSessionIds();
            if (sessionIds != null) {
                sessionIds.remove(session.getId());
                //进入满员房间导致的强制断开，不通知。
                if (sessionIds.size() < 4) {
                    WebSocketUtil.sendMessage(sessionIds, WebsocketResponseBuilder.builderSuccess("玩家" + userName + "离开了游戏"));
                }
                //如果四人都断开了连接，则解散该桌
                //只有最后退出的人处于自由状态，其他异常退出的人会处于重连状态.重连失败后，进入自由状态。
                if (sessionIds.size() == 0) {
                    redisTemplateMapper.delUserTable(String.valueOf(userId));
                    redisTemplateMapper.delRelateTableSession(tableId);
                    redisTemplateMapper.delRelateTableUser(tableId);
                } else {
                    redisTemplateMapper.setRelateTableSession(relateTableSession);
                }
            }
        }
    }

    @Override
    public void onError(Session session, Throwable throwable) {

    }
}
