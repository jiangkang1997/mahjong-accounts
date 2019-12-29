package com.jk.mahjongaccounts.service;

import javax.websocket.Session;

/**
 * @author jk
 */
public interface WebSocketService {

    /**
     * 创建新连接
     * @param tableId
     * @param userId
     * @param userName
     * @param session
     * @throws Exception
     */
    void onOpen(String tableId, Integer userId, Integer userName, Session session) throws Exception;

    /**
     * 收到新消息
     * @param message
     * @param session
     */
    void onMessage(String message, Session session) throws Exception;

    /**
     * 链接关闭
     * @param tableId
     * @param userId
     * @param userName
     * @param session
     * @throws Exception
     */
    void onClose(String tableId, Integer userId, Integer userName,Session session) throws Exception;

    /**
     * 连接错误
     * @param session
     * @param throwable
     */
    void onError(Session session, Throwable throwable);



}
