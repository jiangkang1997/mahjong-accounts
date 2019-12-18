package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.model.User;

/**
 * @author jk
 */
public interface UserService {

    /**
     * 注册
     * @param userName
     * @param password
     * @throws Exception
     */
    void register(String userName,String password) throws Exception;

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    User login(String userName,String password) throws Exception;
}
