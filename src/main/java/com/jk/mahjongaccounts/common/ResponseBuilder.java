package com.jk.mahjongaccounts.common;

import java.io.Serializable;

/**
 * @author jk
 */
public class ResponseBuilder implements Serializable {

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

    public static String builderSuccess(){
        return new ResponseBuilder(0,"success").toString();
    }

    public static String builderFail(String message){
        return  new ResponseBuilder(-1,message).toString();
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
