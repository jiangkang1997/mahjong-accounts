package com.jk.mahjongaccounts.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jk
 */
@Data
@NoArgsConstructor
public class AccountInfo {

    private String providerId;

    private String winnerId;

    private String tableId;

    private Map<String, Integer> redouble;

    private List<Gang> gangs;

    public AccountInfo(String providerId, String winnerId, String tableId, Map<String, Object> redoubleMap, List<Gang> gangs) {
        this.providerId = providerId;
        this.winnerId = winnerId;
        this.tableId = tableId;
        this.gangs = gangs;
        this.redouble = new HashMap<>();
        redoubleMap.forEach((key,value) -> this.redouble.put(key,Integer.parseInt((String) value)));
    }
}
