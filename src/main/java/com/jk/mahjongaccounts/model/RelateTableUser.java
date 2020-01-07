package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author jk
 */
@Data
public class RelateTableUser {

    private String tableId;

    private String tableName;

    private Set<User> users;

    public RelateTableUser(){
        this.tableId = System.currentTimeMillis()+"";
    }
}
