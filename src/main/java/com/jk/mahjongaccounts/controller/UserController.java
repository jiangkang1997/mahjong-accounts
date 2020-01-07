package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import com.jk.mahjongaccounts.common.RoleCheck;
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
    public HttpResponseBuilder login(String userName, String password, HttpSession session){
        if(StringUtils.isEmpty(userName)  || StringUtils.isEmpty(password)){
            return HttpResponseBuilder.builderFail("账号或密码不能为空");
        }
        User user;
        try {
            user = userService.login(userName, password);
            session.setAttribute("user",user);
        }catch (BusinessException e){
            return HttpResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess(user);
    }

    @PostMapping("/register")
    public HttpResponseBuilder register(String userName, String password){
        if(StringUtils.isEmpty(userName)  || StringUtils.isEmpty(password)){
            return HttpResponseBuilder.builderFail("账号或密码不能为空");
        }
        try {
            userService.register(userName, password);
        }catch (BusinessException e){
            return HttpResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess();
    }

    @PostMapping("/isUserExist")
    public HttpResponseBuilder isUserExist(String userName){
        if(StringUtils.isEmpty(userName)){
            return HttpResponseBuilder.builderFail("参数不能为空");
        }
        Boolean exist;
        try {
            exist = userService.isUserExist(userName);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess(exist);
    }

    @RoleCheck
    @PostMapping("/getUser")
    public HttpResponseBuilder getUser(HttpSession session){
        try{
            User user = (User) session.getAttribute("user");
            return HttpResponseBuilder.builderSuccess(user);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }


    }
}
