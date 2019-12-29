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
 * 权限控制  切面实现
 * @author jk
 */

@Aspect
@Component
public class RoleCheckAspect {

    @Pointcut(value = "@annotation(com.jk.mahjongaccounts.common.RoleCheck)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        if(!validate()){
            return HttpResponseBuilder.builderFail("登录状态失效，请重新登录");
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }

    private boolean validate(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session=attr.getRequest().getSession(true);
        User user = (User) session.getAttribute("user");
        return user != null;
    }

}
