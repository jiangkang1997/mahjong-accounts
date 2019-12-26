package com.jk.mahjongaccounts.common;

import java.lang.annotation.*;


/**
 * 权限控制注解
 * @author jk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoleCheck {

    String[] roles() default {};

}
