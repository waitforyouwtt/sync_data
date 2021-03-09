package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.DaRoleDataAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * （角色和数据权限关联表）(DaRoleDataAuth)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-03 16:30:55
 */
@Mapper
public interface DaRoleDataAuthDao extends BaseMapper<DaRoleDataAuth> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DaRoleDataAuth queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DaRoleDataAuth> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param daRoleDataAuth 实例对象
     * @return 对象列表
     */
    List<DaRoleDataAuth> queryAll(DaRoleDataAuth daRoleDataAuth);

    /**
     * 新增数据
     *
     * @param daRoleDataAuth 实例对象
     * @return 影响行数
     */
    int insert(DaRoleDataAuth daRoleDataAuth);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRoleDataAuth> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DaRoleDataAuth> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRoleDataAuth> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DaRoleDataAuth> entities);

    /**
     * 修改数据
     *
     * @param daRoleDataAuth 实例对象
     * @return 影响行数
     */
    int update(DaRoleDataAuth daRoleDataAuth);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}