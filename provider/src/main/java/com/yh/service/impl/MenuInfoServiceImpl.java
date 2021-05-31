package com.yh.service.impl;

import com.yh.dao.MenuInfoDao;
import com.yh.entity.MenuInfo;
import com.yh.service.MenuInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (MenuInfo)表服务实现类
 *
 * @author makejava
 * @since 2021-02-22 17:53:52
 */
@Service("menuInfoService")
public class MenuInfoServiceImpl implements MenuInfoService {
    @Resource
    private MenuInfoDao menuInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MenuInfo queryById(Object id) {
        return this.menuInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<MenuInfo> queryAllByLimit(int offset, int limit) {
        return this.menuInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param menuInfo 实例对象
     * @return 实例对象
     */
    @Override
    public MenuInfo insert(MenuInfo menuInfo) {
        this.menuInfoDao.insert(menuInfo);
        return menuInfo;
    }

    /**
     * 修改数据
     *
     * @param menuInfo 实例对象
     * @return 实例对象
     */
    @Override
    public MenuInfo update(MenuInfo menuInfo) {
        this.menuInfoDao.update(menuInfo);
        return this.queryById(menuInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Object id) {
        return this.menuInfoDao.deleteById(id) > 0;
    }

    @Override
    public List<MenuInfo> findMenus(){
        MenuInfo info = new MenuInfo();
        info.setIsDelete(0);
        return this.menuInfoDao.queryAll(info);
    }

    @Override
    public List<Long> findCountMenus() {
        return menuInfoDao.findCountMenus();
    }

    @Override
    public List<MenuInfo> findMenuBetweenIds(Long min, Long max) {
        return menuInfoDao.findMenuBetweenIds(min,max);
    }

    @Override
    public List<MenuInfo> findMenuPage(Integer offset, Integer num) {
        return menuInfoDao.findMenuPage(offset,num);
    }
}