package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.PageFilter;
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

    @PageFilter
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PageFilter
    @GetMapping("/table")
    public String table(){
        return "table";
    }

    @PageFilter
    @GetMapping("/bills")
    public String bills(){
        return "bills";
    }
}
