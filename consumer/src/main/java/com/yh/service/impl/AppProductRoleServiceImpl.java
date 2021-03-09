package com.yh.service.impl;

import com.yh.dao.AppProductRoleDao;
import com.yh.entity.AppProductRole;
import com.yh.service.AppProductRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用角色表(AppProductRole)表服务实现类
 *
 * @author makejava
 * @since 2021-02-25 11:28:27
 */
@Service("appProductRoleService")
public class AppProductRoleServiceImpl implements AppProductRoleService {
    @Resource
    private AppProductRoleDao appProductRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppProductRole queryById(Integer id) {
        return this.appProductRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppProductRole> queryAllByLimit(int offset, int limit) {
        return this.appProductRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appProductRole 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductRole insert(AppProductRole appProductRole) {
        this.appProductRoleDao.insert(appProductRole);
        return appProductRole;
    }

    /**
     * 修改数据
     *
     * @param appProductRole 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductRole update(AppProductRole appProductRole) {
        this.appProductRoleDao.update(appProductRole);
        return this.queryById(appProductRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.appProductRoleDao.deleteById(id) > 0;
    }
}