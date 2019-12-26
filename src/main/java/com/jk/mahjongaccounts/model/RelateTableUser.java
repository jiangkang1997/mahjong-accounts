package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.util.List;

/**
 * @author jk
 */
@Data
public class RelateTableUser {

    private String tableId;

    private String tableName;

    private List<User> users;

    public RelateTableUser(){
        this.tableId = System.currentTimeMillis()+"";
    }
}
