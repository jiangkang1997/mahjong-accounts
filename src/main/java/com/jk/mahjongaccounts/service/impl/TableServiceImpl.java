package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.RedisKey;
import com.jk.mahjongaccounts.mapper.RedisTemplateMapper;
import com.jk.mahjongaccounts.model.RelateTableUser;
import com.jk.mahjongaccounts.model.User;
import com.jk.mahjongaccounts.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public synchronized String creatTable(User user){
        RelateTableUser relateTableUser = new RelateTableUser();
        Set<User> users = new HashSet<>();
        relateTableUser.setTableName(user.getUserName()+"创建的房间");
        relateTableUser.setUsers(users);
        redisTemplateMapper.setRelateTableUser(relateTableUser);
        return relateTableUser.getTableId();
    }

    @Override
    public synchronized void exit(User user,String tableId) throws Exception {
        RelateTableUser relateTableUser = redisTemplateMapper.getRelateTableUser(tableId);
        if(relateTableUser == null){
            throw new BusinessException("该局游戏已解散");
        }
        Set<User> users = relateTableUser.getUsers();
        users.remove(user);
        relateTableUser.setUsers(users);
        redisTemplateMapper.setRelateTableUser(relateTableUser);
    }

    @Override
    public synchronized void join(User user,String tableId) throws Exception {
        RelateTableUser relateTableUser = redisTemplateMapper.getRelateTableUser(tableId);
        if(relateTableUser == null){
            throw new BusinessException("该局游戏已解散");
        }
        Set<User> users = relateTableUser.getUsers();
        users.add(user);
        if(users.size() > TABLE_MAX_SIZE){
            throw new BusinessException("本桌人数已满");
        }
        relateTableUser.setUsers(users);
        redisTemplateMapper.setRelateTableUser(relateTableUser);
        redisTemplateMapper.setUserTable(String.valueOf(user.getUserId()), tableId);
    }

    @Override
    public List<RelateTableUser> getAll(){
        Set<String> allTable = redisTemplateMapper.getAllTable();
        List<RelateTableUser> result = new ArrayList<>();
        for (String s : allTable) {
            result.add(redisTemplateMapper.getRelateTableUser(s.substring(RedisKey.TABLE_USER.length())));
        }
        return result;
    }

    @Override
    public String reconnect(Integer userId) throws Exception{
        String tableId =  redisTemplateMapper.getUserTable(String.valueOf(userId));
        if(tableId == null){
            throw new BusinessException("无法连接到该房间");
        }
        //需要判断房间是否存在
        RelateTableUser relateTableUser = redisTemplateMapper.getRelateTableUser(tableId);
        if(relateTableUser == null){
            //将用户置为自由状态（非重连状态）
            redisTemplateMapper.delUserTable(String.valueOf(userId));
            throw new BusinessException("该房间已解散 无法连接");
        }
        return relateTableUser.getTableId();
    }

    @Override
    public boolean isGaming(Integer userId) {
        String tableId = redisTemplateMapper.getUserTable(String.valueOf(userId));
        return tableId != null;
    }

    @Override
    public String getTableId(Integer userId) {
        return redisTemplateMapper.getUserTable(String.valueOf(userId));
    }

    @Override
    public List<User> getPlayer(String tableId) {
        RelateTableUser relateTableUser = redisTemplateMapper.getRelateTableUser(tableId);
        Set<User> users = relateTableUser.getUsers();
        return new ArrayList<>(users);
    }

}
