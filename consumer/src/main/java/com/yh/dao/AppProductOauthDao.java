package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppProductOauth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * oauth协议应用扩展表(AppProductOauth)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-01 11:04:01
 */
@Mapper
public interface AppProductOauthDao extends BaseMapper<AppProductOauth> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductOauth queryById(Object id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductOauth> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appProductOauth 实例对象
     * @return 对象列表
     */
    List<AppProductOauth> queryAll(AppProductOauth appProductOauth);

    /**
     * 新增数据
     *
     * @param appProductOauth 实例对象
     * @return 影响行数
     */
    int insert(AppProductOauth appProductOauth);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductOauth> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppProductOauth> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductOauth> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppProductOauth> entities);

    /**
     * 修改数据
     *
     * @param appProductOauth 实例对象
     * @return 影响行数
     */
    int update(AppProductOauth appProductOauth);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}