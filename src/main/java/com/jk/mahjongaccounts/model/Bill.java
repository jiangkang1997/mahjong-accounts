package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 */
@Data
public class Bill implements Serializable {

    /**
     * 账单id
     */
    private Integer billId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 桌id
     */
    private String tableId;

    /**
     * 游戏id
     */
    private String gameId;

    /**
     * 本局收益
     */
    private Integer profit;

    public Bill(){
        this.gameId = String.valueOf(System.currentTimeMillis());
    }

    public Bill(Integer userId, String tableId){
        this.gameId = String.valueOf(System.currentTimeMillis());
        this.userId = userId;
        this.tableId = tableId;
    }
}