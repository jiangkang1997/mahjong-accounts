package com.jk.mahjongaccounts.mapper;


import com.jk.mahjongaccounts.model.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author jk
 */
@Mapper
@Component
public interface BillMapper {


    /**
     * 删除
     * @param billId
     * @return
     */
    int deleteByPrimaryKey(@Param("billId") Integer billId);

    /**
     * 插入
     * @param bill
     * @return
     */
    int insert(@Param("bill") Bill bill);


    /**
     * 根据主键查找
     * @param billId
     * @return
     */
    Bill selectByPrimaryKey(@Param("billId") Integer billId);

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(Bill record);
}