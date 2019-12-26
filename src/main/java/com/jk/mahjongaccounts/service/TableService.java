package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.model.RelateTableUser;
import com.jk.mahjongaccounts.model.User;

import java.util.List;

/**
 * @author jk
 */
public interface TableService {

    /**
     * 创建房间
     * @param user
     * @throws Exception
     */
    String creatTable(User user) throws Exception;

    /**
     * 退出房间
     * @param user
     * @param tableId
     */
    void exit(User user,String tableId) throws Exception;

    /**
     * 加入房间
     * @param user
     * @param tableId 游戏桌id
     * @throws Exception
     */
    void join(User user,String tableId) throws Exception;

    /**
     * 获取所有房间
     */
    List<RelateTableUser> getAll() throws Exception;
}
