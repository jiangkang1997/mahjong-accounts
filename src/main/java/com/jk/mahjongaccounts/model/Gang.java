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
     * false:暗杠   true：明杠
     */
    private boolean isPublic;

    /**
     * 赢家（用户id）
     */
    private String winner;

    /**
     * 输家（用户id）
     */
    private String loser;
}
