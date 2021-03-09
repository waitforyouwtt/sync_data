package com.yh.service;

import com.yh.entity.RelationUserRole;

import java.util.List;

/**
 * (RelationUserRole)表服务接口
 *
 * @author makejava
 * @since 2021-03-04 14:25:13
 */
public interface RelationUserRoleService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RelationUserRole queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RelationUserRole> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param relationUserRole 实例对象
     * @return 实例对象
     */
    RelationUserRole insert(RelationUserRole relationUserRole);

    /**
     * 修改数据
     *
     * @param relationUserRole 实例对象
     * @return 实例对象
     */
    RelationUserRole update(RelationUserRole relationUserRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    List<RelationUserRole> findRelationUserRoles(Integer min, Integer max);

    List<Integer> findIds(Integer min, Integer max);

    List<Integer> findCountRelationUserRoles();
}