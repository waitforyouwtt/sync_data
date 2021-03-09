package com.yh.service;

import com.yh.entity.AppUserRole;

import java.util.List;

/**
 * 用户-角色表(AppUserRole)表服务接口
 *
 * @author makejava
 * @since 2021-03-04 14:59:08
 */
public interface AppUserRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppUserRole queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppUserRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appUserRole 实例对象
     * @return 实例对象
     */
    AppUserRole insert(AppUserRole appUserRole);

    /**
     * 修改数据
     *
     * @param appUserRole 实例对象
     * @return 实例对象
     */
    AppUserRole update(AppUserRole appUserRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}