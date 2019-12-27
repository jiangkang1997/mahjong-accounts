package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.RedisKey;
import com.jk.mahjongaccounts.common.RedisTemplateMapper;
import com.jk.mahjongaccounts.model.RelateTableUser;
import com.jk.mahjongaccounts.model.User;
import com.jk.mahjongaccounts.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 */
@Service
public class TableServiceImpl implements TableService {

    private final RedisTemplateMapper redisTemplateMapper;
    private final Integer TABLE_MAX_SIZE = 4;

    @Autowired
    public TableServiceImpl(RedisTemplateMapper redisTemplateMapper) {
        this.redisTemplateMapper = redisTemplateMapper;
    }

    @Override
    public String creatTable(User user) throws Exception {
        RelateTableUser relateTableUser = new RelateTableUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        relateTableUser.setTableName(user.getUserName()+"创建的房间");
        relateTableUser.setUsers(users);
        redisTemplateMapper.push(RedisKey.TABLE_USER_HASH, relateTableUser.getTableId(), relateTableUser);
        return relateTableUser.getTableId();
    }

    @Override
    public void exit(User user,String tableId) throws Exception {
        RelateTableUser relateTableUser = redisTemplateMapper.getByTableId(RedisKey.TABLE_USER_HASH,tableId,RelateTableUser.class);
        if(relateTableUser == null){
            throw new BusinessException("该局游戏已解散");
        }
        List<User> users = relateTableUser.getUsers();
        users.remove(user);
        relateTableUser.setUsers(users);
        redisTemplateMapper.push(RedisKey.TABLE_USER_HASH, relateTableUser.getTableId(), relateTableUser);
    }

    @Override
    public synchronized void join(User user,String tableId) throws Exception {
        RelateTableUser relateTableUser = redisTemplateMapper.getByTableId(RedisKey.TABLE_USER_HASH,tableId,RelateTableUser.class);
        if(relateTableUser == null){
            throw new BusinessException("该局游戏已解散");
        }
        List<User> users = relateTableUser.getUsers();
        if(users.size() >= TABLE_MAX_SIZE){
            throw new BusinessException("本桌人数已满");
        }
        users.add(user);
        relateTableUser.setUsers(users);
        redisTemplateMapper.push(RedisKey.TABLE_USER_HASH, relateTableUser.getTableId(), relateTableUser);
    }

    @Override
    public List<RelateTableUser> getAll() throws Exception {
        return redisTemplateMapper.getAllRelateTableUser();
    }

    @Override
    public String reconnect(Integer userId) throws Exception{
        String tableId =  redisTemplateMapper.getTableId(String.valueOf(userId));
        if(tableId == null){
            throw new BusinessException("无法连接到该房间");
        }
        //需要判断房间是否存在
        RelateTableUser relateTableUser = redisTemplateMapper.getByTableId(RedisKey.TABLE_USER_HASH, tableId, RelateTableUser.class);
        if(relateTableUser == null){
            //将用户置为自由状态（非重连状态）
            redisTemplateMapper.del(String.valueOf(userId));
            throw new BusinessException("该房间已解散 无法连接");
        }
        return relateTableUser.getTableId();
    }

    @Override
    public boolean isGaming(Integer userId) {
        String tableId = redisTemplateMapper.getTableId(String.valueOf(userId));
        return tableId != null;
    }


}
