package com.jk.mahjongaccounts.common;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author jk
 */
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public <T> void set(String key ,T value){
        //任意类型转换成String
        String val = beanToString(value);
        stringRedisTemplate.opsForValue().set(key,val);
    }

    public <T> T get(String key,Class<T> clazz){
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return stringToBean(value,clazz);
        }catch (Exception e){
            return null ;
        }
    }

    public void del(String key){
        stringRedisTemplate.delete(key);
    }

    public Set<String> keys(String prefix){
        return stringRedisTemplate.keys(prefix + "*");
    }

    public <T> void set(String key,T value,Long time){
        //任意类型转换成String
        String val = beanToString(value);
        stringRedisTemplate.opsForValue().set(key,val,time, TimeUnit.MINUTES);
    }

    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if(value==null||value.length()<=0||clazz==null){
            return null;
        }
        if(clazz ==int.class ||clazz==Integer.class){
            return (T)Integer.valueOf(value);
        }
        else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(value);
        }
        else if(clazz==String.class){
            return (T)value;
        }else {
            return JSON.toJavaObject(JSON.parseObject(value),clazz);
        }
    }

    /**
     * 将任意类型转换为 String型
     * @param value
     * @param <T>
     * @return
     */
    private <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class <?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }
        else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }
        else if(clazz==String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }
}
