package com.jk.mahjongaccounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jk
 */
@Controller
public class RootController {

    @GetMapping("/")
    public String login(){
        return "login";
    }
}
