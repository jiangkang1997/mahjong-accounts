package com.jk.mahjongaccounts.common;

import lombok.NoArgsConstructor;

/**
 * 自定义业务异常类
 * @author jk
 */
public class BusinessException extends Exception {

    public BusinessException(){
    }

    public BusinessException(String message){
        super(message);
    }

}
