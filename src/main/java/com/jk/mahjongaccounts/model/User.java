package com.jk.mahjongaccounts.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jk
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    public User(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

}