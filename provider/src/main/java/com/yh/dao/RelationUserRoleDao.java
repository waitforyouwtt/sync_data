package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.RelationUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (RelationUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-04 14:25:13
 */
@Mapper
public interface RelationUserRoleDao extends BaseMapper<RelationUserRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RelationUserRole queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RelationUserRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param relationUserRole 实例对象
     * @return 对象列表
     */
    List<RelationUserRole> queryAll(RelationUserRole relationUserRole);

    /**
     * 新增数据
     *
     * @param relationUserRole 实例对象
     * @return 影响行数
     */
    int insert(RelationUserRole relationUserRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RelationUserRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RelationUserRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RelationUserRole> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RelationUserRole> entities);

    /**
     * 修改数据
     *
     * @param relationUserRole 实例对象
     * @return 影响行数
     */
    int update(RelationUserRole relationUserRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<RelationUserRole> queryBetweenId(@Param("min") Integer min,@Param("max") Integer max);

    List<Integer> queryIds(@Param("min") Integer min,@Param("max") Integer max);

    List<Integer> findCountRelationUserRoles();
}