package com.yh.controller;

import com.yh.entity.AppRoleResource;
import com.yh.service.AppRoleResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 角色资源表(AppRoleResource)表控制层
 *
 * @author makejava
 * @since 2021-02-25 17:45:27
 */
@RestController
@RequestMapping("appRoleResource")
public class AppRoleResourceController {
    /**
     * 服务对象
     */
    @Resource
    private AppRoleResourceService appRoleResourceService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AppRoleResource selectOne(Object id) {
        return this.appRoleResourceService.queryById(id);
    }

}