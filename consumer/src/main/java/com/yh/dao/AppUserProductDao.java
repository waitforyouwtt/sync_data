package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppUserProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户-应用表(AppUserProduct)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-01 19:08:04
 */
@Mapper
public interface AppUserProductDao extends BaseMapper<AppUserProduct> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppUserProduct queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppUserProduct> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appUserProduct 实例对象
     * @return 对象列表
     */
    List<AppUserProduct> queryAll(AppUserProduct appUserProduct);

    /**
     * 新增数据
     *
     * @param appUserProduct 实例对象
     * @return 影响行数
     */
    int insert(AppUserProduct appUserProduct);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppUserProduct> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppUserProduct> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppUserProduct> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppUserProduct> entities);

    /**
     * 修改数据
     *
     * @param appUserProduct 实例对象
     * @return 影响行数
     */
    int update(AppUserProduct appUserProduct);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}