package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppTenantInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户信息表(AppTenantInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-01 16:40:52
 */
@Mapper
public interface AppTenantInfoDao extends BaseMapper<AppTenantInfo> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppTenantInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppTenantInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appTenantInfo 实例对象
     * @return 对象列表
     */
    List<AppTenantInfo> queryAll(AppTenantInfo appTenantInfo);

    /**
     * 新增数据
     *
     * @param appTenantInfo 实例对象
     * @return 影响行数
     */
    int insert(AppTenantInfo appTenantInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppTenantInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppTenantInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppTenantInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppTenantInfo> entities);

    /**
     * 修改数据
     *
     * @param appTenantInfo 实例对象
     * @return 影响行数
     */
    int update(AppTenantInfo appTenantInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}