package com.yh.service;

import com.yh.entity.AppProductOauth;

import java.util.List;

/**
 * oauth协议应用扩展表(AppProductOauth)表服务接口
 *
 * @author makejava
 * @since 2021-03-01 11:04:01
 */
public interface AppProductOauthService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductOauth queryById(Object id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductOauth> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appProductOauth 实例对象
     * @return 实例对象
     */
    AppProductOauth insert(AppProductOauth appProductOauth);

    /**
     * 修改数据
     *
     * @param appProductOauth 实例对象
     * @return 实例对象
     */
    AppProductOauth update(AppProductOauth appProductOauth);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Object id);

}