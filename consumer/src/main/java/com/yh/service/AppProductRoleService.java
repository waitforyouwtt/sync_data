package com.yh.service;

import com.yh.entity.AppProductRole;

import java.util.List;

/**
 * 应用角色表(AppProductRole)表服务接口
 *
 * @author makejava
 * @since 2021-02-25 11:28:27
 */
public interface AppProductRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductRole queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appProductRole 实例对象
     * @return 实例对象
     */
    AppProductRole insert(AppProductRole appProductRole);

    /**
     * 修改数据
     *
     * @param appProductRole 实例对象
     * @return 实例对象
     */
    AppProductRole update(AppProductRole appProductRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}