package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author jk
 */
@Data
public class RelateTableSession implements Serializable {

    private String tableId;

    private String tableName;

    private Set<String> sessionIds;

    public RelateTableSession(){
        this.tableId = System.currentTimeMillis()+"";
    }

    public RelateTableSession(String tableId){
        this();
        this.tableId = tableId;
    }
}
