package com.yh.service;

import com.yh.entity.AppProductResource;

import java.util.List;

/**
 * 应用资源表(AppProductResource)表服务接口
 *
 * @author makejava
 * @since 2021-02-24 10:35:38
 */
public interface AppProductResourceService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductResource queryById(int id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductResource> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appProductResource 实例对象
     * @return 实例对象
     */
    AppProductResource insert(AppProductResource appProductResource);

    /**
     * 修改数据
     *
     * @param appProductResource 实例对象
     * @return 实例对象
     */
    AppProductResource update(AppProductResource appProductResource);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(int id);
}