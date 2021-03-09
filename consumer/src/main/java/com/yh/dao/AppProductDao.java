package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用表(AppProduct)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-01 11:03:05
 */
@Mapper
public interface AppProductDao extends BaseMapper<AppProduct> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProduct queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProduct> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appProduct 实例对象
     * @return 对象列表
     */
    List<AppProduct> queryAll(AppProduct appProduct);

    /**
     * 新增数据
     *
     * @param appProduct 实例对象
     * @return 影响行数
     */
    int insert(AppProduct appProduct);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProduct> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppProduct> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProduct> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppProduct> entities);

    /**
     * 修改数据
     *
     * @param appProduct 实例对象
     * @return 影响行数
     */
    int update(AppProduct appProduct);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}