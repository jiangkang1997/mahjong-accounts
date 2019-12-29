package com.jk.mahjongaccounts.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author jk
 */
@Data
public class AccountInfo {

    private String providerId;

    private String winnerId;

    private String tableId;

    private Map<String,Integer> redouble;

    private List<Gang> gangs;
}
