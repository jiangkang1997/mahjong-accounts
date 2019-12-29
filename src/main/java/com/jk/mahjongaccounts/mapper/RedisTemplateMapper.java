package com.jk.mahjongaccounts.mapper;

import com.jk.mahjongaccounts.common.RedisUtil;
import com.jk.mahjongaccounts.model.RelateTableSession;
import com.jk.mahjongaccounts.model.RelateTableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author jk
 */
@Component
public class RedisTemplateMapper {

    private final RedisUtil redisUtil;

    @Autowired
    public RedisTemplateMapper(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public RelateTableUser getRelateTableUser(String tableId){
        String key = "RelateTableUser"+tableId;
        return redisUtil.get(key,RelateTableUser.class);
    }

    public void setRelateTableUser(RelateTableUser relateTableUser){
        String key = "RelateTableUser"+relateTableUser.getTableId();
        redisUtil.set(key,relateTableUser);
    }

    public void delRelateTableUser(String tableId){
        String key = "RelateTableUser"+tableId;
        redisUtil.del(key);
    }

    public RelateTableSession getRelateTableSession(String tableId){
        String key = "RelateTableSession"+tableId;
        return redisUtil.get(key,RelateTableSession.class);
    }

    public void setRelateTableSession(RelateTableSession relateTableSession){
        String key = "RelateTableSession"+relateTableSession.getTableId();
        redisUtil.set(key,relateTableSession);
    }

    public void delRelateTableSession(String tableId){
        String key = "RelateTableSession"+tableId;
        redisUtil.del(key);
    }

    public void setUserTable(String userId,String tableId){
        redisUtil.set(userId,tableId);
    }

    public String getUserTable(String userId){
        return redisUtil.get(userId,String.class);
    }

    public void delUserTable(String userId){
        redisUtil.del(userId);
    }

    public Set<String> getAllTable(){
        return redisUtil.keys("RelateTableUser");
    }


}
