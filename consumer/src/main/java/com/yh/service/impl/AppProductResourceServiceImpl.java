package com.yh.service.impl;

import com.yh.dao.AppProductResourceDao;
import com.yh.entity.AppProductResource;
import com.yh.service.AppProductResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用资源表(AppProductResource)表服务实现类
 *
 * @author makejava
 * @since 2021-02-24 10:35:38
 */
@Service("appProductResourceService")
public class AppProductResourceServiceImpl implements AppProductResourceService {
    @Resource
    private AppProductResourceDao appProductResourceDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppProductResource queryById(int id) {
        return this.appProductResourceDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppProductResource> queryAllByLimit(int offset, int limit) {
        return this.appProductResourceDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appProductResource 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductResource insert(AppProductResource appProductResource) {
        this.appProductResourceDao.insert(appProductResource);
        return appProductResource;
    }

    /**
     * 修改数据
     *
     * @param appProductResource 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductResource update(AppProductResource appProductResource) {
        this.appProductResourceDao.update(appProductResource);
        return this.queryById(appProductResource.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(int id) {
        return this.appProductResourceDao.deleteById(id) > 0;
    }
}