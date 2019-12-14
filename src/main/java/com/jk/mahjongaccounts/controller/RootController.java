package com.jk.mahjongaccounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jk
 */
@RestController("/root")
public class RootController {

    @GetMapping("/test")
    public String test(){
        return test();
    }
}
