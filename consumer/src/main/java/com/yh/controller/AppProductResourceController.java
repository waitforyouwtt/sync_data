package com.yh.controller;

import com.yh.entity.AppProductResource;
import com.yh.service.AppProductResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 应用资源表(AppProductResource)表控制层
 *
 * @author makejava
 * @since 2021-02-24 10:35:38
 */
@RestController
@RequestMapping("appProductResource")
public class AppProductResourceController {
    /**
     * 服务对象
     */
    @Resource
    private AppProductResourceService appProductResourceService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AppProductResource selectOne(int id) {
        return this.appProductResourceService.queryById(id);
    }

}