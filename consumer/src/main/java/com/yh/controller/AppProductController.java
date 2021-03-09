package com.yh.controller;

import com.yh.entity.AppProduct;
import com.yh.service.AppProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 应用表(AppProduct)表控制层
 *
 * @author makejava
 * @since 2021-03-01 11:03:05
 */
@RestController
@RequestMapping("appProduct")
public class AppProductController {
    /**
     * 服务对象
     */
    @Resource
    private AppProductService appProductService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public AppProduct selectOne(Integer id) {
        return this.appProductService.queryById(id);
    }

}