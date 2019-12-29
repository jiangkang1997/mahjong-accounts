package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.model.AccountInfo;

/**
 * @author jk
 */
public interface AccountService {

    /**
     * 提交记账信息
     * @param accountInfo
     * @return 该桌已经提交的人数
     */
    Integer account(AccountInfo accountInfo);

    /**
     * 计算本桌的帐
     * @param tableId
     * @throws Exception
     * @return
     */
    void calculation(String tableId) throws Exception;
}
