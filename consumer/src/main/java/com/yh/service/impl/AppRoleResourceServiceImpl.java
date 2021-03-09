package com.yh.service.impl;

import com.yh.dao.AppRoleResourceDao;
import com.yh.entity.AppRoleResource;
import com.yh.service.AppRoleResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色资源表(AppRoleResource)表服务实现类
 *
 * @author makejava
 * @since 2021-02-25 17:45:26
 */
@Service("appRoleResourceService")
public class AppRoleResourceServiceImpl implements AppRoleResourceService {
    @Resource
    private AppRoleResourceDao appRoleResourceDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppRoleResource queryById(Object id) {
        return this.appRoleResourceDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppRoleResource> queryAllByLimit(int offset, int limit) {
        return this.appRoleResourceDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appRoleResource 实例对象
     * @return 实例对象
     */
    @Override
    public AppRoleResource insert(AppRoleResource appRoleResource) {
        this.appRoleResourceDao.insert(appRoleResource);
        return appRoleResource;
    }

    /**
     * 修改数据
     *
     * @param appRoleResource 实例对象
     * @return 实例对象
     */
    @Override
    public AppRoleResource update(AppRoleResource appRoleResource) {
        this.appRoleResourceDao.update(appRoleResource);
        return this.queryById(appRoleResource.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Object id) {
        return this.appRoleResourceDao.deleteById(id) > 0;
    }
}