package com.jk.mahjongaccounts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jk
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/table")
    public String table(){
        return "table";
    }
}
