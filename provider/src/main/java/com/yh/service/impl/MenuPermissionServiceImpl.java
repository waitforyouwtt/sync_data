package com.yh.service.impl;

import com.yh.dao.MenuPermissionDao;
import com.yh.entity.MenuPermission;
import com.yh.service.MenuPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (MenuPermission)表服务实现类
 *
 * @author makejava
 * @since 2021-02-23 11:18:16
 */
@Service("menuPermissionService")
public class MenuPermissionServiceImpl implements MenuPermissionService {
    @Resource
    private MenuPermissionDao menuPermissionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MenuPermission queryById(int id) {
        return this.menuPermissionDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<MenuPermission> queryAllByLimit(int offset, int limit) {
        return this.menuPermissionDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param menuPermission 实例对象
     * @return 实例对象
     */
    @Override
    public MenuPermission insert(MenuPermission menuPermission) {
        this.menuPermissionDao.insert(menuPermission);
        return menuPermission;
    }

    /**
     * 修改数据
     *
     * @param menuPermission 实例对象
     * @return 实例对象
     */
    @Override
    public MenuPermission update(MenuPermission menuPermission) {
        this.menuPermissionDao.update(menuPermission);
        return this.queryById(menuPermission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.menuPermissionDao.deleteById(id) > 0;
    }

    @Override
    public List<MenuPermission> findMenuPermissions() {
        MenuPermission params = new MenuPermission();
        params.setIsDelete(0);
        return menuPermissionDao.queryAll(params);
    }

    @Override
    public List<Integer> findCountPermission() {
        return menuPermissionDao.findCountPermission();
    }

    @Override
    public List<MenuPermission> menuPermissionBetweenIds(Integer min, Integer max) {
        return menuPermissionDao.menuPermissionBetweenIds(min,max);
    }
}