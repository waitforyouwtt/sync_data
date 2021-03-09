package com.yh.controller;

import com.yh.entity.RoleInfo;
import com.yh.service.RoleInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (RoleInfo)表控制层
 *
 * @author makejava
 * @since 2021-02-25 10:17:04
 */
@RestController
@RequestMapping("roleInfo")
public class RoleInfoController {
    /**
     * 服务对象
     */
    @Resource
    private RoleInfoService roleInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public RoleInfo selectOne(Long id) {
        return this.roleInfoService.queryById(id);
    }

}