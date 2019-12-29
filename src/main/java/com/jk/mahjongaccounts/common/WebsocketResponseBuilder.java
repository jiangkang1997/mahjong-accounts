package com.jk.mahjongaccounts.common;

import lombok.Data;

/**
 * @author jk
 */
@Data
public class WebsocketResponseBuilder {

    /**
     * 返回码  0：成功  -1：失败
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    private WebsocketResponseBuilder(int status, String message) {
        this.code = status;
        this.message = message;
    }

    public static WebsocketResponseBuilder builderSuccess(String msg){
        return new WebsocketResponseBuilder(0,msg);
    }

    public static WebsocketResponseBuilder builderFail(String message){
        return new WebsocketResponseBuilder(-1,message);
    }
}
