package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppRoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色资源表(AppRoleResource)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-25 17:45:26
 */
@Mapper
public interface AppRoleResourceDao extends BaseMapper<AppRoleResource> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppRoleResource queryById(Object id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppRoleResource> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appRoleResource 实例对象
     * @return 对象列表
     */
    List<AppRoleResource> queryAll(AppRoleResource appRoleResource);

    /**
     * 新增数据
     *
     * @param appRoleResource 实例对象
     * @return 影响行数
     */
    int insert(AppRoleResource appRoleResource);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppRoleResource> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppRoleResource> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppRoleResource> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppRoleResource> entities);

    /**
     * 修改数据
     *
     * @param appRoleResource 实例对象
     * @return 影响行数
     */
    int update(AppRoleResource appRoleResource);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}