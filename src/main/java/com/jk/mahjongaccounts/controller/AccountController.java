package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.ResponseBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jk
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    public ResponseBuilder account(){
        return ResponseBuilder.builderSuccess();
    }
}
