package com.yh.service;

import com.yh.entity.RoleInfo;

import java.util.List;

/**
 * (RoleInfo)表服务接口
 *
 * @author makejava
 * @since 2021-02-25 10:17:04
 */
public interface RoleInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoleInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RoleInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param roleInfo 实例对象
     * @return 实例对象
     */
    RoleInfo insert(RoleInfo roleInfo);

    /**
     * 修改数据
     *
     * @param roleInfo 实例对象
     * @return 实例对象
     */
    RoleInfo update(RoleInfo roleInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    List<RoleInfo> findRoleInfos();

    List<String> findCountRoles();

    List<RoleInfo> findRoleBetweenIds(Integer min, Integer max);
}