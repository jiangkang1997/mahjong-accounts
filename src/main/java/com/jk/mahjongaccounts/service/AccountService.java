package com.jk.mahjongaccounts.service;

import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.model.BillVo;

import java.util.List;

/**
 * @author jk
 */
public interface AccountService {

    /**
     * 提交记账信息
     * @param accountInfo
     */
    void submit(AccountInfo accountInfo);

    /**
     * 获取桌的总账
     * @param tableId
     * @return
     */
    List<BillVo> getAccount(String tableId);

}
