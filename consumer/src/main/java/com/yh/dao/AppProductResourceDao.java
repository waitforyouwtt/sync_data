package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppProductResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用资源表(AppProductResource)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-24 10:35:38
 */
@Mapper
public interface AppProductResourceDao extends BaseMapper<AppProductResource> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductResource queryById(int id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductResource> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appProductResource 实例对象
     * @return 对象列表
     */
    List<AppProductResource> queryAll(AppProductResource appProductResource);

    /**
     * 新增数据
     *
     * @param appProductResource 实例对象
     * @return 影响行数
     */
    int insert(AppProductResource appProductResource);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductResource> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppProductResource> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductResource> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppProductResource> entities);

    /**
     * 修改数据
     *
     * @param appProductResource 实例对象
     * @return 影响行数
     */
    int update(AppProductResource appProductResource);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

    List<AppProductResource> selectOne2(@Param("productCode") String productCode,@Param("tenantCode") String tenantCode,@Param("resourceCode") String resourceCode);
}