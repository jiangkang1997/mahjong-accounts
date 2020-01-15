package com.jk.mahjongaccounts.common;


import java.lang.annotation.*;

/**
 * 页面拦截注解
 * @author jk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageFilter {

}
