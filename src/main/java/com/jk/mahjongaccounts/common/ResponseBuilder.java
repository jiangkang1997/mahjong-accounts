package com.jk.mahjongaccounts.common;

import lombok.Data;

/**
 * @author jk
 */
@Data
public class ResponseBuilder{

    /**
     * 返回码  0：成功  -1：失败
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    private ResponseBuilder(int status, String description) {
        this.code = status;
        this.message = description;
    }

    public static ResponseBuilder builderSuccess(){
        return new ResponseBuilder(0,"success");
    }

    public static ResponseBuilder builderFail(String message){
        return  new ResponseBuilder(-1,message);
    }
}
