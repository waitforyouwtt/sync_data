package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.RelationRoleMenuPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (RelationRoleMenuPermission)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-25 14:48:37
 */
@Mapper
public interface RelationRoleMenuPermissionDao extends BaseMapper<RelationRoleMenuPermission> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RelationRoleMenuPermission queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RelationRoleMenuPermission> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 对象列表
     */
    List<RelationRoleMenuPermission> queryAll(RelationRoleMenuPermission relationRoleMenuPermission);

    /**
     * 新增数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 影响行数
     */
    int insert(RelationRoleMenuPermission relationRoleMenuPermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RelationRoleMenuPermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RelationRoleMenuPermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RelationRoleMenuPermission> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RelationRoleMenuPermission> entities);

    /**
     * 修改数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 影响行数
     */
    int update(RelationRoleMenuPermission relationRoleMenuPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据条件获取id区间角色资源集合
     * @param min
     * @param max
     * @return
     */
    List<RelationRoleMenuPermission> queryBetweenId(@Param("min")Integer min,@Param("max")Integer max);

    /**
     * 获取角色资源总条数
     * @return
     */
    List<Integer> findCountRelationRoleMenuPermissions();

    List<RelationRoleMenuPermission> findCountRelationRoleMenuPermissions2();
}