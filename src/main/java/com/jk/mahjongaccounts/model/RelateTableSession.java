package com.jk.mahjongaccounts.model;

import lombok.Data;

import javax.websocket.Session;
import java.util.List;

/**
 * @author jk
 */
@Data
public class RelateTableSession {

    private String tableId;

    private String tableName;

    private List<Session> sessions;

    public RelateTableSession(){
        this.tableId = System.currentTimeMillis()+"";
    }

    public RelateTableSession(String tableId){
        this.tableId = System.currentTimeMillis()+"";
        this.tableId = tableId;
    }
}
