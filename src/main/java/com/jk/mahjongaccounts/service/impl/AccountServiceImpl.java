package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.mapper.RedisTemplateMapper;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.model.Gang;
import com.jk.mahjongaccounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jk
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final RedisTemplateMapper redisTemplateMapper;

    @Autowired
    public AccountServiceImpl(RedisTemplateMapper redisTemplateMapper) {
        this.redisTemplateMapper = redisTemplateMapper;
    }

    @Override
    public Integer account(AccountInfo accountInfo) {
        redisTemplateMapper.setAccountInfo(accountInfo);
        return redisTemplateMapper.getAccountInfoSize(accountInfo.getTableId());
    }

    @Override
    public void calculation(String tableId) throws Exception {
        List<AccountInfo> accountInfos = redisTemplateMapper.getAccountInfos(tableId);
        if(accountInfos.size() != 4){
            throw new BusinessException("还有人未提交，无法继续");
        }
        //先检查四人输入信息的合法性
        checkLegality(accountInfos);
    }


    private void checkLegality(List<AccountInfo> accountInfos) throws Exception{
        //检查胡牌的合法性 保证四人输入的winner是同一个人
        //检查开杠的合法性，保证四人输入的开杠情况一致
        String winner = accountInfos.get(0).getWinnerId();
        Map<String,Integer> redouble = accountInfos.get(0).getRedouble();
        for (AccountInfo accountInfo : accountInfos) {
            if(!accountInfo.getWinnerId().equals(winner)){
                throw new BusinessException("胡牌者不统一，请检查重新输入");
            }
            if(!accountInfo.getRedouble().equals(redouble)){
                throw new BusinessException("开杠情况不统一，请检查重新输入");
            }
        }
        //检查 明杠,暗杠 的合法性，保证明杠双方的对应关系正确以及暗杠没有恶意输入
        Map<String,Integer> publicGangMap = new HashMap<>(8);
        Map<String,Integer> privateGangMap = new HashMap<>(8);
        for (AccountInfo accountInfo : accountInfos) {
            List<Gang> gangs = accountInfo.getGangs();
            if (gangs != null && gangs.size() > 0) {
                for (Gang gang : gangs) {
                    if (gang.isPublic()) {
                        Integer num = publicGangMap.put(gang.getWinner() + "_" + gang.getLoser(), 1);
                        if (num != null) {
                            publicGangMap.put(gang.getWinner() + "_" + gang.getLoser(), num + 1);
                        }
                    } else {
                        Integer num = privateGangMap.put(gang.getWinner(), 1);
                        if (num != null) {
                            privateGangMap.put(gang.getWinner(), num + 1);
                        }
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : publicGangMap.entrySet()) {
                if (entry.getValue() % 2 != 0) {
                    throw new BusinessException("明杠的对应关系不一致，请检查重新输入");
                }
            }
            for (Map.Entry<String, Integer> entry : privateGangMap.entrySet()) {
                if (entry.getValue() % 4 != 0) {
                    throw new BusinessException("暗杠情况输入不统一，请检查重新输入");
                }
            }

        }
    }
}
