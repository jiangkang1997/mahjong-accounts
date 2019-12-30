package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.model.AccountInfo;

/**
 * @author jk
 */
public interface AccountService {

    /**
     * 提交记账信息
     * @param accountInfo
     */
    void submit(AccountInfo accountInfo);

}
