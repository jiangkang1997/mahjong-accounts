package com.jk.mahjongaccounts.mapper;

import com.jk.mahjongaccounts.common.RedisKey;
import com.jk.mahjongaccounts.common.RedisUtil;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.model.RelateTableSession;
import com.jk.mahjongaccounts.model.RelateTableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        String key = RedisKey.TABLE_USER + tableId;
        return redisUtil.get(key,RelateTableUser.class);
    }

    public void setRelateTableUser(RelateTableUser relateTableUser){
        String key = RedisKey.TABLE_USER + relateTableUser.getTableId();
        redisUtil.set(key,relateTableUser);
    }

    public void delRelateTableUser(String tableId){
        String key = RedisKey.TABLE_USER + tableId;
        redisUtil.del(key);
    }

    public RelateTableSession getRelateTableSession(String tableId){
        String key = RedisKey.TABLE_SESSION + tableId;
        return redisUtil.get(key,RelateTableSession.class);
    }

    public void setRelateTableSession(RelateTableSession relateTableSession){
        String key = RedisKey.TABLE_SESSION + relateTableSession.getTableId();
        redisUtil.set(key,relateTableSession);
    }

    public void delRelateTableSession(String tableId){
        String key = RedisKey.TABLE_SESSION + tableId;
        redisUtil.del(key);
    }

    public void setUserTable(String userId,String tableId){
        String key = RedisKey.USER_TABLE + userId;
        redisUtil.set(key,tableId);
    }

    public String getUserTable(String userId){
        String key = RedisKey.USER_TABLE + userId;
        return redisUtil.get(key,String.class);
    }

    public void delUserTable(String userId){
        String key = RedisKey.USER_TABLE + userId;
        redisUtil.del(key);
    }

    public Set<String> getAllTable(){
        return redisUtil.keys(RedisKey.TABLE_USER);
    }

    public void setAccountInfo(AccountInfo accountInfo){
        String key = RedisKey.ACCOUNT_INFO + accountInfo.getTableId() + "_" + accountInfo.getProviderId();
        redisUtil.set(key,accountInfo,20L);
    }

    public AccountInfo getAccountInfo(String tableId,String userId){
        String key = RedisKey.ACCOUNT_INFO + tableId + "_" + userId;
        return redisUtil.get(key,AccountInfo.class);
    }

    public List<AccountInfo> getAccountInfos(String tableId){
        Set<String> keys = redisUtil.keys(RedisKey.ACCOUNT_INFO + tableId + "_*");
        List<AccountInfo> result = new ArrayList<>();
        for (String key : keys) {
            result.add(redisUtil.get(key, AccountInfo.class));
        }
        return result;
    }

    public Integer getAccountInfoSize(String tableId){
        Set<String> keys = redisUtil.keys(RedisKey.ACCOUNT_INFO + tableId + "_*");
        return keys.size();
    }


}
