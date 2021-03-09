package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户-角色表(AppUserRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-04 14:59:07
 */
@Mapper
public interface AppUserRoleDao extends BaseMapper<AppUserRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppUserRole queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppUserRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appUserRole 实例对象
     * @return 对象列表
     */
    List<AppUserRole> queryAll(AppUserRole appUserRole);

    /**
     * 新增数据
     *
     * @param appUserRole 实例对象
     * @return 影响行数
     */
    int insert(AppUserRole appUserRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppUserRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppUserRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppUserRole> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppUserRole> entities);

    /**
     * 修改数据
     *
     * @param appUserRole 实例对象
     * @return 影响行数
     */
    int update(AppUserRole appUserRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}