package com.yh.controller;

import com.yh.entity.AppProductRole;
import com.yh.service.AppProductRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 应用角色表(AppProductRole)表控制层
 *
 * @author makejava
 * @since 2021-02-25 11:28:27
 */
@RestController
@RequestMapping("appProductRole")
public class AppProductRoleController {
    /**
     * 服务对象
     */
    @Resource
    private AppProductRoleService appProductRoleService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AppProductRole selectOne(Integer id) {
        return this.appProductRoleService.queryById(id);
    }

}