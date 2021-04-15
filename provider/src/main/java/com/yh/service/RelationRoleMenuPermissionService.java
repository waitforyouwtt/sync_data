package com.yh.service;

import com.yh.entity.RelationRoleMenuPermission;
import com.yh.view.ProductRoleVO;

import java.util.List;

/**
 * (RelationRoleMenuPermission)表服务接口
 *
 * @author makejava
 * @since 2021-02-25 14:48:37
 */
public interface RelationRoleMenuPermissionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RelationRoleMenuPermission queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RelationRoleMenuPermission> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 实例对象
     */
    RelationRoleMenuPermission insert(RelationRoleMenuPermission relationRoleMenuPermission);

    /**
     * 修改数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 实例对象
     */
    RelationRoleMenuPermission update(RelationRoleMenuPermission relationRoleMenuPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过角色id 倒推productCode
     * @param roleId
     * @return
     */
    List<String> findProductCodeByRoleId(String roleId);
    List<ProductRoleVO> findProductCodeByRoleIds(String roleIds);

    /**
     *根据条件获取id区间角色资源集合
     * @return
     */
    List<RelationRoleMenuPermission> findRelationRoleMenuPermissions(Integer min,Integer max);

    /**
     * 获取角色资源总条数
     * @return
     */
    List<Integer> findCountRelationRoleMenuPermissions();

    List<RelationRoleMenuPermission> findRelationRoleMenuPermissions2();
}