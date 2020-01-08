package com.jk.mahjongaccounts.controller;

import com.jk.mahjongaccounts.common.BusinessException;
import com.jk.mahjongaccounts.common.HttpResponseBuilder;
import com.jk.mahjongaccounts.common.RoleCheck;
import com.jk.mahjongaccounts.model.RelateTableUser;
import com.jk.mahjongaccounts.model.User;
import com.jk.mahjongaccounts.service.TableService;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 创建房间
     * @param session
     * @return
     */
    @RoleCheck
    @RequestMapping("/creat")
    public HttpResponseBuilder creatTable(HttpSession session){
        String tableId;
        try {
            User user = (User) session.getAttribute("user");
            tableId = tableService.creatTable(user);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess(tableId);
    }

    /**
     * 退出房间
     * @param tableId
     * @param session
     * @return
     */
    @RoleCheck
    @RequestMapping("/exit")
    public HttpResponseBuilder exit(String tableId, HttpSession session){
        if(StringUtils.isEmpty(tableId)){
            return HttpResponseBuilder.builderFail("参数不可为空");
        }
        User user = (User) session.getAttribute("user");
        try {
            tableService.exit(user,tableId);
        } catch (BusinessException e){
            return HttpResponseBuilder.builderFail(e.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess();
    }

    /**
     * 加入房间
     * @param tableId
     * @param session
     * @return
     */
    @RoleCheck
    @RequestMapping("/join")
    public HttpResponseBuilder join(String tableId, HttpSession session){
        if(StringUtils.isEmpty(tableId)){
            return HttpResponseBuilder.builderFail("参数不可为空");
        }
        User user = (User) session.getAttribute("user");
        try {
            tableService.join(user,tableId);
        } catch (BusinessException e){
            return HttpResponseBuilder.builderFail(e.getMessage());
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
        return HttpResponseBuilder.builderSuccess();
    }

    /**
     * 获取所有房间
     * @return
     */
    @RoleCheck
    @RequestMapping("/getAll")
    public HttpResponseBuilder getAll(){
        try {
            List<RelateTableUser> all = tableService.getAll();
            return HttpResponseBuilder.builderSuccess(all);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }

    /**
     * 重新连接房间
     * @return
     */
    @RoleCheck
    @RequestMapping("/reconnect")
    public HttpResponseBuilder reconnect(Integer userId){
        try {
            String tableId = tableService.reconnect(userId);
            return HttpResponseBuilder.builderSuccess(tableId);
        }catch (BusinessException e){
            return HttpResponseBuilder.builderFail(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }

    /***
     * 判断用户是否在游戏中（掉线重连）
     * @param userId
     * @return
     */
    @RoleCheck
    @RequestMapping("/isGaming")
    public HttpResponseBuilder isGaming(Integer userId){
        try {
            boolean result = tableService.isGaming(userId);
            return HttpResponseBuilder.builderSuccess(result);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }

    /**
     * 获取用户所在桌id
     * @param userId
     * @return
     */
    @RoleCheck
    @RequestMapping("/getTableId")
    public HttpResponseBuilder getTableId(Integer userId){
        try {
            String result = tableService.getTableId(userId);
            return HttpResponseBuilder.builderSuccess(result);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }

    /**
     * 获取用户所在桌id
     * @param tableId
     * @return
     */
    @RoleCheck
    @RequestMapping("/getPlayer")
    public HttpResponseBuilder getPlayer(String tableId){
        try {
            List<String> result = tableService.getPlayer(tableId);
            return HttpResponseBuilder.builderSuccess(result);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponseBuilder.builderFail("系统错误");
        }
    }
}
