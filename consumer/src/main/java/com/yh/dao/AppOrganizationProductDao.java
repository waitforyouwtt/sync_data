package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppOrganizationProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织架构-应用表(AppOrganizationProduct)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-01 14:15:55
 */
@Mapper
public interface AppOrganizationProductDao extends BaseMapper<AppOrganizationProduct> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppOrganizationProduct queryById(Object id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppOrganizationProduct> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appOrganizationProduct 实例对象
     * @return 对象列表
     */
    List<AppOrganizationProduct> queryAll(AppOrganizationProduct appOrganizationProduct);

    /**
     * 新增数据
     *
     * @param appOrganizationProduct 实例对象
     * @return 影响行数
     */
    int insert(AppOrganizationProduct appOrganizationProduct);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppOrganizationProduct> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppOrganizationProduct> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppOrganizationProduct> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppOrganizationProduct> entities);

    /**
     * 修改数据
     *
     * @param appOrganizationProduct 实例对象
     * @return 影响行数
     */
    int update(AppOrganizationProduct appOrganizationProduct);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}