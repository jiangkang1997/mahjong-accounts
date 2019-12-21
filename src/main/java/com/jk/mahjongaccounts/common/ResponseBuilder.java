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
     * 返回的数据
     */
    private Object data;

    /**
     * 返回消息
     */
    private String message;

    private ResponseBuilder(int status, Object data,String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    public static ResponseBuilder builderSuccess(){
        return new ResponseBuilder(0,null,"success");
    }

    public static ResponseBuilder builderSuccess(Object data){
        return new ResponseBuilder(0,data,"success");
    }

    public static ResponseBuilder builderFail(String message){
        return  new ResponseBuilder(-1,null,message);
    }
}
