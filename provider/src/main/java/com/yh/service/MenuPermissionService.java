package com.yh.service;

import com.yh.entity.MenuPermission;

import java.math.BigInteger;
import java.util.List;

/**
 * (MenuPermission)表服务接口
 *
 * @author makejava
 * @since 2021-02-23 11:18:15
 */
public interface MenuPermissionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MenuPermission queryById(BigInteger id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<MenuPermission> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param menuPermission 实例对象
     * @return 实例对象
     */
    MenuPermission insert(MenuPermission menuPermission);

    /**
     * 修改数据
     *
     * @param menuPermission 实例对象
     * @return 实例对象
     */
    MenuPermission update(MenuPermission menuPermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);


    List<MenuPermission> findMenuPermissions();

    /**
     * 获取按钮总条数
     * @return
     */
    List<Long> findCountPermission();

    /**
     * 根据条件获取id区间按钮集合
     * @param min
     * @param max
     * @return
     */
    List<MenuPermission> menuPermissionBetweenIds(Long min, Long max);
}