package com.yh.controller;

import com.yh.entity.MenuPermission;
import com.yh.service.MenuPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * (MenuPermission)表控制层
 *
 * @author makejava
 * @since 2021-02-23 11:18:16
 */
@RestController
@RequestMapping("menuPermission")
public class MenuPermissionController {
    /**
     * 服务对象
     */
    @Resource
    private MenuPermissionService menuPermissionService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public MenuPermission selectOne(BigInteger id) {
        return this.menuPermissionService.queryById(id);
    }

}