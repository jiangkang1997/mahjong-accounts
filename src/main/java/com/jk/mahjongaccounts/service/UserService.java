package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.model.User;

/**
 * @author jk
 */
public interface UserService {

    /**
     * 注册
     * @param userName
     * @param password
     * @throws BusinessException
     */
     void register(String userName,String password) throws BusinessException;

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     * @throws BusinessException
     */
    User login(String userName,String password) throws BusinessException;

    /**
     * 检查用户是否存在
     * @param userName
     * @return
     * @throws Exception
     */
    boolean isUserExist(String userName);
}
