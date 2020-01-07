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
     * @return 创建的桌id
     */
    String creatTable(User user);

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
    List<RelateTableUser> getAll();

    /**
     * 重新连接
     * @param userId
     * @return 房间号
     * @throws Exception
     */
    String reconnect(Integer userId) throws Exception;

    /**
     * 判断用户是否在游戏中（掉线重连）
     * @param userId
     * @return
     * @throws Exception
     */
    boolean isGaming(Integer userId);

    /**
     * 获取用户所在桌id
     * @param userId
     * @return
     */
    String getTableId(Integer userId);
}
