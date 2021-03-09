package com.yh.service;


import com.yh.entity.MenuInfo;

import java.util.List;

/**
 * (MenuInfo)表服务接口
 *
 * @author makejava
 * @since 2021-02-22 17:53:51
 */
public interface MenuInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MenuInfo queryById(Object id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<MenuInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param menuInfo 实例对象
     * @return 实例对象
     */
    MenuInfo insert(MenuInfo menuInfo);

    /**
     * 修改数据
     *
     * @param menuInfo 实例对象
     * @return 实例对象
     */
    MenuInfo update(MenuInfo menuInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Object id);

    List<MenuInfo> findMenus();

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
    List<MenuInfo> findMenuBetweenIds(Integer min, Integer max);

    /**
     *
     * @param offset
     * @param num
     * @return
     */
    List<MenuInfo> findMenuPage(Integer offset, Integer num);
}