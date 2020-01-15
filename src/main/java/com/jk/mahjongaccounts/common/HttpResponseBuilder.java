package com.jk.mahjongaccounts.common;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 */
@Data
public class HttpResponseBuilder implements Serializable {

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

    private HttpResponseBuilder(int status, Object data, String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    public static HttpResponseBuilder builderSuccess(){
        return new HttpResponseBuilder(0,null,"success");
    }

    public static HttpResponseBuilder builderSuccess(Object data){
        return new HttpResponseBuilder(0,data,"success");
    }

    public static HttpResponseBuilder builderFail(String message){
        return  new HttpResponseBuilder(-1,null,message);
    }
}
