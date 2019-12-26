package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.ResponseBuilder;
import com.jk.mahjongaccounts.common.RoleCheck;
import com.jk.mahjongaccounts.model.Table;
import com.jk.mahjongaccounts.model.User;
import com.jk.mahjongaccounts.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.BuilderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author jk
 */
@RestController
@RequestMapping("/table")
@Slf4j
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @RoleCheck
    @RequestMapping("/creat")
    public ResponseBuilder creatTable(HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            tableService.creatTable(user);
        }catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }

    @RoleCheck
    @RequestMapping("/exit")
    public ResponseBuilder exit(String tableId,HttpSession session){
        if(StringUtils.isEmpty(tableId)){
            return ResponseBuilder.builderFail("参数不可为空");
        }
        User user = (User) session.getAttribute("user");
        try {
            tableService.exit(user,tableId);
        } catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }

    @RoleCheck
    @RequestMapping("/join")
    public ResponseBuilder join(String tableId,HttpSession session){
        if(StringUtils.isEmpty(tableId)){
            return ResponseBuilder.builderFail("参数不可为空");
        }
        User user = (User) session.getAttribute("user");
        try {
            tableService.join(user,tableId);
        } catch (BusinessException e){
            return ResponseBuilder.builderFail(e.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
        return ResponseBuilder.builderSuccess();
    }

    @RoleCheck
    @RequestMapping("/getAll")
    public ResponseBuilder getAll(){
        try {
            List<Table> all = tableService.getAll();
            return ResponseBuilder.builderSuccess(all);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBuilder.builderFail("系统错误");
        }
    }
}
