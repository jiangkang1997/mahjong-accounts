package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.RoleCheck;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @RoleCheck
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @RoleCheck
    @GetMapping("/table")
    public String table(){
        return "table";
    }

    @RoleCheck
    @GetMapping("/bills")
    public String bills(){
        return "bills";
    }
}
