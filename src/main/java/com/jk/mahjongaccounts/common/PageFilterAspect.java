package com.jk.mahjongaccounts.common;

import com.jk.mahjongaccounts.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 页面拦截切面
 * 未登陆时，将注解拦截的页面强制跳转至登录页
 * @author jk
 */
@Aspect
@Component
public class PageFilterAspect {

    @Pointcut(value = "@annotation(com.jk.mahjongaccounts.common.PageFilter)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        if(!validate()){
            return "login";
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return "login";
        }
    }

    private boolean validate(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session=attr.getRequest().getSession(true);
        User user = (User) session.getAttribute("user");
        return user != null;
    }
}
