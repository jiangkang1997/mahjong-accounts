package com.jk.mahjongaccounts.common;

import com.alibaba.fastjson.JSON;
import com.jk.mahjongaccounts.model.RelateTableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 */
@Component
public class RedisTemplateMapper {

    @Autowired
    public StringRedisTemplate redisTemplate;

    public <T> void push(String key,String field,T t) throws Exception {
        String val = beanToString(t);
        if (val == null || val.length() <= 0) {
            throw new BusinessException("不可传空值");
        }
        redisTemplate.opsForHash().put(key,field,val);
    }


    public List<RelateTableUser> getAll() throws Exception {
        List<Object> tables = redisTemplate.opsForHash().values(RedisKey.TABLE_USER_HASH);
        List<RelateTableUser> result = new ArrayList<>();
        for (Object table : tables) {
            result.add(stringToBean((String) table, RelateTableUser.class));
        }
        return result;
    }

    public <T> T getByTableId(String key, String tableId,Class<T> clazz) throws Exception{
        String table = (String) redisTemplate.opsForHash().get(key,tableId);
        return stringToBean(table, clazz);
    }


    @SuppressWarnings("unchecked")
    private <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || value.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else {
            return JSON.toJavaObject(JSON.parseObject(value), clazz);
        }
    }

    /**
     * 将任意类型转换为 String型
     *
     * @param value
     * @param <T>
     * @return
     */
    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }
}
