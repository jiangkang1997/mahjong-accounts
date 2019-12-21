package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.ResponseBuilder;
import com.jk.mahjongaccounts.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jk
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseBuilder login(String userName, String password){
        if(userName == null || password == null){
            return ResponseBuilder.builderFail("账号或密码不能为空");
        }
        try {
            userService.login(userName, password);
        }catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }

    @PostMapping("/register")
    public ResponseBuilder register(String userName, String password){
        if(userName == null || password == null){
            return ResponseBuilder.builderFail("账号或密码不能为空");
        }
        try {
            userService.register(userName, password);
        }catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }
}
