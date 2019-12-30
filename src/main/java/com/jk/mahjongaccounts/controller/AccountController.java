package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jk
 */
@RestController
@RequestMapping("/account")
@Slf4j
@EnableAsync
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/submit")
    public HttpResponseBuilder submit(AccountInfo accountInfo){
        try {
            accountService.submit(accountInfo);
            return HttpResponseBuilder.builderSuccess();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误，提交失败");
        }
    }
}
