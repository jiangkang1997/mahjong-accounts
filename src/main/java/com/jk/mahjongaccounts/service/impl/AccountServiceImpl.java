package com.jk.mahjongaccounts.service.impl;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.WebSocketUtil;
import com.jk.mahjongaccounts.common.WebsocketResponseBuilder;
import com.jk.mahjongaccounts.mapper.BillMapper;
import com.jk.mahjongaccounts.mapper.RedisTemplateMapper;
import com.jk.mahjongaccounts.mapper.UserMapper;
import com.jk.mahjongaccounts.model.AccountInfo;
import com.jk.mahjongaccounts.model.Bill;
import com.jk.mahjongaccounts.model.Gang;
import com.jk.mahjongaccounts.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author jk
 */
@Service
@Slf4j
@EnableTransactionManagement
public class AccountServiceImpl implements AccountService {

    private final RedisTemplateMapper redisTemplateMapper;
    private final BillMapper billMapper;
    private final UserMapper userMapper;
    private final Double baseScore = 1d;

    @Autowired
    public AccountServiceImpl(RedisTemplateMapper redisTemplateMapper, BillMapper billMapper, UserMapper userMapper) {
        this.redisTemplateMapper = redisTemplateMapper;
        this.billMapper = billMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void submit(AccountInfo accountInfo){
        Integer num =  redisTemplateMapper.getAccountInfoSize(accountInfo.getTableId());
        //都已提交的情况下，忽略重复提交，因为已经进入计算状态
        if(num == 4){
            return;
        }
        redisTemplateMapper.setAccountInfo(accountInfo);
        num =  redisTemplateMapper.getAccountInfoSize(accountInfo.getTableId());
        if(num == 4){
            //提交完成，自动进入计算状态
            try{
                calculation(accountInfo.getTableId());
            }catch (Exception e){
                //计算过程出现不可控异常，删除所有提交，并给出警告
                log.error(e.getMessage(),e);
                redisTemplateMapper.delAccountInfos(accountInfo.getTableId());
                WebSocketUtil.sendMessageForTable(accountInfo.getTableId(), WebsocketResponseBuilder.builderFail("计算异常，请所有人重新提交"));
            }
        }else if(num > 4){
            //数据异常情况,需要删掉所有提交，并给出警告
            redisTemplateMapper.delAccountInfos(accountInfo.getTableId());
            WebSocketUtil.sendMessageForTable(accountInfo.getTableId(), WebsocketResponseBuilder.builderFail("数据异常，请所有人重新提交"));
        }
    }


    @Transactional(rollbackFor = Exception.class)
    void calculation(String tableId) throws Exception{
        List<AccountInfo> accountInfos = redisTemplateMapper.getAccountInfos(tableId);
        if(accountInfos.size() != 4){
            //为了避免数据不完整 或数据异常导致的计算异常，需删除所有提交，并给出警告
            redisTemplateMapper.delAccountInfos(tableId);
            WebSocketUtil.sendMessageForTable(tableId, WebsocketResponseBuilder.builderFail("数据异常，请所有人重新提交"));
            return;
        }
        try {
            //先检查四人输入信息的合法性
            checkLegality(accountInfos);
            //生成账单 通过合法性检查后，不会出现账不平的情况
            List<Bill> bills = creatBill(accountInfos);
            //账单持久化
            billMapper.insertBatch(bills);
            //给出账单
            sendBill(bills);
            //todo:删除已提交的所有账单信息
        }catch (BusinessException e){
            WebSocketUtil.sendMessageForTable(tableId, WebsocketResponseBuilder.builderFail(e.getMessage()));
        }
    }

    /**
     * 检查主逻辑
     * @param accountInfos
     * @throws Exception
     */
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

    /**
     * 算账主逻辑 根据输入，给出每人账单
     * @param accountInfos
     * @return
     */
    private List<Bill> creatBill(List<AccountInfo> accountInfos){
        Map<String,Bill> billMap = new HashMap<>(8);
        for (AccountInfo accountInfo : accountInfos) {
            Bill bill = new Bill(Integer.parseInt(accountInfo.getProviderId()),accountInfo.getTableId());
            billMap.put(String.valueOf(bill.getUserId()),bill);
        }
        //开杠情况
        Map<String,Integer> redouble = accountInfos.get(0).getRedouble();
        //先算胡牌
        String winner = accountInfos.get(0).getWinnerId();
        int count = 0;
        for (AccountInfo accountInfo : accountInfos) {
            if(!accountInfo.getProviderId().equals(winner)){
                Bill bill = billMap.get(accountInfo.getProviderId());
                double cost = baseScore
                        * Math.pow(2,redouble.get(accountInfo.getProviderId()))
                        * Math.pow(2,redouble.get(winner));
                count += cost;
                bill.setProfit(bill.getProfit() + (-1 * cost) );
            }
        }
        Bill winnerBill = billMap.get(winner);
        winnerBill.setProfit(winnerBill.getProfit() + count);
        //明杠和暗杠
        for (AccountInfo accountInfo : accountInfos) {
            for (Gang gang : accountInfo.getGangs()) {
                //明杠
                if(gang.isPublic()){
                    Bill gangWinnerBill = billMap.get(gang.getWinner());
                    Bill gangLoserBill = billMap.get(gang.getLoser());
                    double cost = baseScore * 1/2
                            * Math.pow(2,redouble.get(gang.getWinner()))
                            * Math.pow(2,redouble.get(gang.getLoser()));
                    gangWinnerBill.setProfit(gangWinnerBill.getProfit() + cost);
                    gangLoserBill.setProfit(gangLoserBill.getProfit() - cost);
                }
                //暗杠
                else {
                    Bill gangBill = billMap.get(gang.getWinner());
                    int gangCount  = 0;
                    for (AccountInfo info : accountInfos) {
                        if(!info.getProviderId().equals(gang.getWinner())){
                            Bill bill = billMap.get(info.getProviderId());
                            double cost = baseScore
                                    * Math.pow(2,redouble.get(info.getProviderId()))
                                    * Math.pow(2,redouble.get(gang.getWinner()));
                            bill.setProfit(bill.getProfit() + (-1 * cost) );
                            gangCount += cost;
                        }
                    }
                    gangBill.setProfit(gangBill.getProfit() + gangCount);
                }
            }
        }
        List<Bill> billList = new ArrayList<>();
        billMap.forEach((key,value) -> billList.add(value));
        return billList;
    }

    /**
     * 给所有人发送核对账单
     * @param bills
     * @throws Exception
     */
    private void sendBill(List<Bill> bills) throws Exception {
        if(bills == null || bills.size() != 4){
            throw new Exception("系统错误：数据异常");
        }
        StringBuilder message = new StringBuilder();
        for (Bill bill : bills) {
            String userName = userMapper.getByUserId(bill.getUserId()).getUserName();
            message.append(userName).append(" : ").append(bill.getProfit()).append("元").append("\n");
        }
        String tableId = bills.get(0).getTableId();
        WebSocketUtil.sendMessageForTable(tableId,WebsocketResponseBuilder.builderSuccess(message.toString()));
    }
}
