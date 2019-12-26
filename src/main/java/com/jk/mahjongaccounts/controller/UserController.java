package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.ResponseBuilder;
import com.jk.mahjongaccounts.model.User;
import com.jk.mahjongaccounts.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    public ResponseBuilder login(String userName, String password, HttpSession session){
        if(StringUtils.isEmpty(userName)  || StringUtils.isEmpty(password)){
            return ResponseBuilder.builderFail("账号或密码不能为空");
        }
        User user;
        try {
            user = userService.login(userName, password);
            session.setAttribute("user",user);
        }catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess(user);
    }

    @PostMapping("/register")
    public ResponseBuilder register(String userName, String password){
        if(StringUtils.isEmpty(userName)  || StringUtils.isEmpty(password)){
            return ResponseBuilder.builderFail("账号或密码不能为空");
        }
        try {
            userService.register(userName, password);
        }catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }

    @PostMapping("/isUserExist")
    public ResponseBuilder isUserExist(String userName){
        if(StringUtils.isEmpty(userName)){
            return ResponseBuilder.builderFail("参数不能为空");
        }
        Boolean exist;
        try {
            exist = userService.isUserExist(userName);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess(exist);
    }
}
