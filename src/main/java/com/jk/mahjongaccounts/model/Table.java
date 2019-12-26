package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.util.List;

@Data
public class Table {

    private String tableId;

    private String tableName;

    private List<User> users;

    public Table(){
        this.tableId = System.currentTimeMillis()+"";
    }
}
