package com.yh.dao;

import com.yh.entity.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (MenuInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-22 17:53:48
 */
@Mapper
public interface MenuInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MenuInfo queryById(Object id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<MenuInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param menuInfo 实例对象
     * @return 对象列表
     */
    List<MenuInfo> queryAll(MenuInfo menuInfo);

    /**
     * 新增数据
     *
     * @param menuInfo 实例对象
     * @return 影响行数
     */
    int insert(MenuInfo menuInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MenuInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MenuInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MenuInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<MenuInfo> entities);

    /**
     * 修改数据
     *
     * @param menuInfo 实例对象
     * @return 影响行数
     */
    int update(MenuInfo menuInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Object id);

    /**
     * 查询菜单的总条数
     * @return
     */
    List<Integer> findCountMenus();

    /**
     * 根据条件获取id区间菜单集合
     * @param min
     * @param max
     * @return
     */
    List<MenuInfo> findMenuBetweenIds(@Param("min") Integer min,@Param("max") Integer max);

    List<MenuInfo> findMenuPage(@Param("offset") Integer offset,@Param("num") Integer num);
}