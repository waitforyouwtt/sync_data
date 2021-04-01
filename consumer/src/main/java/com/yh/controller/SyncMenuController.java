package com.yh.controller;

import com.yh.service.SyncService;
import com.yh.service.ThreadService2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "SyncMenuController")
@RestController
@RequestMapping("/sync")
public class SyncMenuController {

    @Autowired
    SyncService syncService;

    @Autowired
    ThreadService2 threadService2;

    @ApiOperation(value = "菜单同步资源[数据处理中，请耐心等待]",notes = "菜单同步资源",tags = {"SyncMenuController"})
    @GetMapping("/menu")
    public String syncMenu(){
      return threadService2.menuSyncResource();
    }

    @ApiOperation(value = "按钮同步资源[数据处理中，请耐心等待]",notes = "按钮同步资源",tags = {"SyncMenuController"})
    @GetMapping("/button")
    public String buttonSyncResource(){
        return threadService2.menuPermissionSyncResource();
    }

    @ApiOperation(value = "角色同步[数据处理中，请耐心等待]",notes = "角色同步",tags = {"SyncMenuController"})
    @GetMapping("/role")
    public String roleSync(){
        return threadService2.roleSync();
    }

    @ApiOperation(value = "角色资源同步[数据处理中，请耐心等待]",notes = "角色资源同步",tags = {"SyncMenuController"})
    @GetMapping("/roleResource")
    public String roleResource(){
        return threadService2.roleResource();
    }

    @ApiOperation(value = "用户角色同步[数据处理中，请耐心等待]",notes = "用户角色同步同步",tags = {"SyncMenuController"})
    @GetMapping("/syncRelationUserRoles")
    public String syncRelationUserRoles(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return syncService.syncRelationUserRoles(min,max);
    }

    @ApiOperation(value = "用户角色同步[数据处理中，请耐心等待]",notes = "用户角色同步同步",tags = {"SyncMenuController"})
    @GetMapping("/syncRelationUserRoles3")
    public String syncRelationUserRoles3(@RequestParam("min")Integer min,@RequestParam("max")Integer max){
        return syncService.syncRelationUserRoles3(min,max);
    }

    @ApiOperation(value = "角色资源同步[数据处理中，请耐心等待]",notes = "角色资源同步",tags = {"SyncMenuController"})
    @GetMapping("/syncTenant")
    public String syncTenant(){
        return syncService.syncTenant();
    }


}
