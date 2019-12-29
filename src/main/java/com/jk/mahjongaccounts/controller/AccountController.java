package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jk
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    public HttpResponseBuilder account(){
        return HttpResponseBuilder.builderSuccess();
    }
}
