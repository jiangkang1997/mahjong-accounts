package com.jk.mahjongaccounts.common;

import lombok.Data;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放所有的链接的 session
 * @author jk
 */
@Data
public class SessionMap {

    private static Map<String, Session> data = new ConcurrentHashMap<>();


    public synchronized static void put(String key,Session value){
        data.put(key,value);
    }

    public static Session get(String key){
        return data.get(key);
    }

    public synchronized static void remove(String key){
        data.remove(key);
    }
}
