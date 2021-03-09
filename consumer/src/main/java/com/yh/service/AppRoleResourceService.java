package com.yh.service;

import com.yh.entity.AppRoleResource;

import java.util.List;

/**
 * 角色资源表(AppRoleResource)表服务接口
 *
 * @author makejava
 * @since 2021-02-25 17:45:26
 */
public interface AppRoleResourceService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppRoleResource queryById(Object id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppRoleResource> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appRoleResource 实例对象
     * @return 实例对象
     */
    AppRoleResource insert(AppRoleResource appRoleResource);

    /**
     * 修改数据
     *
     * @param appRoleResource 实例对象
     * @return 实例对象
     */
    AppRoleResource update(AppRoleResource appRoleResource);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Object id);

}