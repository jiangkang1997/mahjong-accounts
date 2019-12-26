package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.RedisTemplateMapper;
import com.jk.mahjongaccounts.model.Table;
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
    public void creatTable(User user) throws Exception {
        Table table = new Table();
        List<User> users = new ArrayList<>();
        users.add(user);
        table.setTableName(user.getUserName()+"创建的房间");
        table.setUsers(users);
        redisTemplateMapper.push(table);
    }

    @Override
    public void exit(User user,String tableId) throws Exception {
        Table table = redisTemplateMapper.getByTableId(tableId);
        if(table == null){
            throw new BusinessException("该局游戏已解散");
        }
        List<User> users = table.getUsers();
        users.remove(user);
        table.setUsers(users);
        redisTemplateMapper.push(table);
    }

    @Override
    public synchronized void join(User user,String tableId) throws Exception {
        Table table = redisTemplateMapper.getByTableId(tableId);
        if(table == null){
            throw new BusinessException("该局游戏已解散");
        }
        List<User> users = table.getUsers();
        if(users.size() >= TABLE_MAX_SIZE){
            throw new BusinessException("本桌人数已满");
        }
        users.add(user);
        table.setUsers(users);
        redisTemplateMapper.push(table);
    }

    @Override
    public List<Table> getAll() throws Exception {
        return redisTemplateMapper.getAll();
    }
}
