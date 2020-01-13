package com.jk.mahjongaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jk
 */
@Data
@AllArgsConstructor
public class BillVo {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 总收益
     */
    private Double profit;
}
