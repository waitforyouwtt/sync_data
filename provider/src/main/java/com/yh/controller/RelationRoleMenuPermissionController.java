package com.yh.controller;

import com.yh.entity.RelationRoleMenuPermission;
import com.yh.service.RelationRoleMenuPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (RelationRoleMenuPermission)表控制层
 *
 * @author makejava
 * @since 2021-02-25 14:48:37
 */
@RestController
@RequestMapping("relationRoleMenuPermission")
public class RelationRoleMenuPermissionController {
    /**
     * 服务对象
     */
    @Resource
    private RelationRoleMenuPermissionService relationRoleMenuPermissionService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public RelationRoleMenuPermission selectOne(Integer id) {
        return this.relationRoleMenuPermissionService.queryById(id);
    }

}