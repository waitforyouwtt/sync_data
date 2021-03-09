package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.MenuPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (MenuPermission)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-23 11:18:15
 */
@Mapper
public interface MenuPermissionDao extends BaseMapper<MenuPermission> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MenuPermission queryById(int id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<MenuPermission> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param menuPermission 实例对象
     * @return 对象列表
     */
    List<MenuPermission> queryAll(MenuPermission menuPermission);

    /**
     * 新增数据
     *
     * @param menuPermission 实例对象
     * @return 影响行数
     */
    int insert(MenuPermission menuPermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MenuPermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MenuPermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MenuPermission> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<MenuPermission> entities);

    /**
     * 修改数据
     *
     * @param menuPermission 实例对象
     * @return 影响行数
     */
    int update(MenuPermission menuPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

    List<Integer> findCountPermission();

    List<MenuPermission> menuPermissionBetweenIds(@Param("min") Integer min,@Param("max") Integer max);
}