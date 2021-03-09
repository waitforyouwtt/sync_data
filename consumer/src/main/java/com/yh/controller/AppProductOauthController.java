package com.yh.controller;

import com.yh.entity.AppProductOauth;
import com.yh.service.AppProductOauthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * oauth协议应用扩展表(AppProductOauth)表控制层
 *
 * @author makejava
 * @since 2021-03-01 11:04:01
 */
@RestController
@RequestMapping("appProductOauth")
public class AppProductOauthController {
    /**
     * 服务对象
     */
    @Resource
    private AppProductOauthService appProductOauthService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AppProductOauth selectOne(Object id) {
        return this.appProductOauthService.queryById(id);
    }

}