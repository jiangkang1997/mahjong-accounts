package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.RedisTemplateService;
import com.jk.mahjongaccounts.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.util.PrimitiveIterator;

/**
 * @author jk
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisTemplateService redisTemplateService;

    @Autowired
    public RedisController(RedisTemplateService redisTemplateService) {
        this.redisTemplateService = redisTemplateService;
    }

    @RequestMapping("/test")
    public String redisTest(){
        User user = new User();
        user.setUserId(11);
        user.setUserName("jk");
        user.setPassword("123456");
        redisTemplateService.set("key1",user);
        User us = redisTemplateService.get("key1",User.class);
        return us.toString();
    }
}
