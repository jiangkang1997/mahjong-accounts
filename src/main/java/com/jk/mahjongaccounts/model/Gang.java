package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述 “杠” 的关系的类
 * @author jk
 */
@Data
public class Gang implements Serializable {

    /**
     * 0:点笑  1：回头笑   2.闷笑
     */
    private Integer gangType;

    /**
     * 赢家（用户id）
     */
    private String winner;

    /**
     * 输家（用户id）
     */
    private String loser;
}
