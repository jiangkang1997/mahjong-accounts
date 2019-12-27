package com.jk.mahjongaccounts.mapper;

import com.jk.mahjongaccounts.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author jk
 */
@Mapper
@Component
public interface UserMapper {


    /**
     * 插入
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    User login(@Param("userName") String userName, @Param("password") String password);

    /**
     * 根据用户名查询用户
     * @param userName
     * @return
     */
    User getByUserName(@Param("userName") String userName);

    /**
     * 根据用id查询用户
     * @param userId
     * @return
     */
    User getByUserId(@Param("userId") Integer userId);


}