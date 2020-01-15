package com.jk.mahjongaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 */
@Data
@AllArgsConstructor
public class BillVo implements Serializable {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 总收益
     */
    private Double profit;
}
