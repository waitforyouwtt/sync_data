package com.yh.service.impl;

import com.yh.dao.AppUserRoleDao;
import com.yh.entity.AppUserRole;
import com.yh.service.AppUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-角色表(AppUserRole)表服务实现类
 *
 * @author makejava
 * @since 2021-03-04 14:59:08
 */
@Service("appUserRoleService")
public class AppUserRoleServiceImpl implements AppUserRoleService {
    @Resource
    private AppUserRoleDao appUserRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppUserRole queryById(Integer id) {
        return this.appUserRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppUserRole> queryAllByLimit(int offset, int limit) {
        return this.appUserRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public AppUserRole insert(AppUserRole appUserRole) {
        this.appUserRoleDao.insert(appUserRole);
        return appUserRole;
    }

    /**
     * 修改数据
     *
     * @param appUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public AppUserRole update(AppUserRole appUserRole) {
        this.appUserRoleDao.update(appUserRole);
        return this.queryById(appUserRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.appUserRoleDao.deleteById(id) > 0;
    }
}