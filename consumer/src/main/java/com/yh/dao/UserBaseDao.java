package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.UserBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 最小化用户信息(UserBase)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-27 17:01:03
 */
@Mapper
public interface UserBaseDao extends BaseMapper<UserBase> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserBase queryById(Object id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserBase> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userBase 实例对象
     * @return 对象列表
     */
    List<UserBase> queryAll(UserBase userBase);

    /**
     * 新增数据
     *
     * @param userBase 实例对象
     * @return 影响行数
     */
    int insert(UserBase userBase);

    /**
     * 修改数据
     *
     * @param userBase 实例对象
     * @return 影响行数
     */
    int update(UserBase userBase);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

}