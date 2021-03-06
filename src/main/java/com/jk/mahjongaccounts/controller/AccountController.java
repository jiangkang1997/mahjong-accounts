package com.jk.mahjongaccounts.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import com.jk.mahjongaccounts.common.RoleCheck;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.model.BillVo;
import com.jk.mahjongaccounts.model.Gang;
import com.jk.mahjongaccounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @RoleCheck
    @RequestMapping("/submit")
    public HttpResponseBuilder submit(String providerId,String winnerId,String tableId,String redouble,String gangs){
        try {
            List<Gang> gangList = JSON.parseArray(gangs, Gang.class);
            System.out.println(gangList);
            JSONObject  jsonObject = JSONObject.parseObject(redouble);
            Map<String,Object> redoubleMap = (Map<String,Object>)jsonObject;
            AccountInfo accountInfo = new AccountInfo(providerId,winnerId,tableId,redoubleMap,gangList);
            accountService.submit(accountInfo);
            return HttpResponseBuilder.builderSuccess();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误，提交失败");
        }
    }

    @RoleCheck
    @RequestMapping("/getAccount")
    public HttpResponseBuilder getAccount(@RequestParam("tableId") String tableId){
        if(StringUtils.isEmpty(tableId)){
            return HttpResponseBuilder.builderFail("参数不可为空");
        }
        try{
            List<BillVo> billVos = accountService.getAccount(tableId);
            return HttpResponseBuilder.builderSuccess(billVos);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }


}
