package com.yh.controller;

import com.yh.service.SingleService;
import com.yh.service.ThreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "ThreadController")
@RestController
@RequestMapping("/thread")
public class ThreadController {

    @Autowired
    ThreadService threadService;

    @Autowired
    SingleService singleService;

    @ApiOperation(value = "菜单同步资源[数据处理中，请耐心等待]",notes = "菜单同步资源",tags = {"ThreadController"})
    @GetMapping("/menu")
    public String syncMenu(){
        return threadService.menuSyncResource();
    }

    @ApiOperation(value = "按钮同步资源[数据处理中，请耐心等待]",notes = "按钮同步资源",tags = {"ThreadController"})
    @GetMapping("/button")
    public String buttonSyncResource(){
        return threadService.menuPermissionSyncResource();
    }

    @ApiOperation(value = "角色同步[数据处理中，请耐心等待]",notes = "角色同步",tags = {"ThreadController"})
    @GetMapping("/role")
    public String roleSync(){
        return threadService.roleSync();
    }

    @ApiOperation(value = "角色资源同步[数据处理中，请耐心等待]",notes = "角色资源同步",tags = {"ThreadController"})
    @GetMapping("/roleResource")
    public String roleResource(){
        return threadService.roleResource();
    }

    @ApiOperation(value = "用户角色同步[数据处理中，请耐心等待]",notes = "用户角色同步同步",tags = {"ThreadController"})
    @GetMapping("/syncRelationUserRoles")
    public String syncRelationUserRoles(){
        return threadService.syncRelationUserRoles();
    }

    @ApiOperation(value = "应用也是资源[数据处理中，请耐心等待]",notes = "应用也是资源同步",tags = {"ThreadController"})
    @GetMapping("/applicationIsResource")
    public String applicationIsResource(){
        return threadService.applicationIsResource();
    }
}
