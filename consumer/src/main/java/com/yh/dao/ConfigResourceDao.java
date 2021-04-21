package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.ConfigResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ConfigResource)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-17 13:44:25
 */
@Mapper
public interface ConfigResourceDao extends BaseMapper<ConfigResource> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ConfigResource queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ConfigResource> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param configResource 实例对象
     * @return 对象列表
     */
    List<ConfigResource> queryAll(ConfigResource configResource);

    /**
     * 新增数据
     *
     * @param configResource 实例对象
     * @return 影响行数
     */
    int insert(ConfigResource configResource);

    /**
     * 修改数据
     *
     * @param configResource 实例对象
     * @return 影响行数
     */
    int update(ConfigResource configResource);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}