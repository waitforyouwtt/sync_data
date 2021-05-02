package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.Role;
import com.yh.entity.RoleInfo;
import com.yh.entity.RoleSplitByApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (RoleInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-25 10:17:04
 */
@Mapper
public interface RoleInfoDao extends BaseMapper<RoleInfo> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RoleInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<RoleInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param roleInfo 实例对象
     * @return 对象列表
     */
    List<RoleInfo> queryAll(RoleInfo roleInfo);

    /**
     * 新增数据
     *
     * @param roleInfo 实例对象
     * @return 影响行数
     */
    int insert(RoleInfo roleInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RoleInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RoleInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RoleInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RoleInfo> entities);

    /**
     * 修改数据
     *
     * @param roleInfo 实例对象
     * @return 影响行数
     */
    int update(RoleInfo roleInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 获取角色的总条数
     * @return
     */
    List<Long> findCountRoles();

    /**
     * 根据条件获取id区间角色集合
     * @param min
     * @param max
     * @return
     */
    List<RoleInfo> findRoleBetweenIds(@Param("min") Long min,@Param("max") Long max);

    /**
     * 获取角色按应用拆分后的数据
     * @return
     */
    List<RoleSplitByApplication> findRoleSplitByApplications();

    /**
     *
     * @param
     * @return
     */
    List<Role> findSyncAbandonList();
}