package com.yh.service.impl;

import com.yh.dao.AppProductOauthDao;
import com.yh.entity.AppProductOauth;
import com.yh.service.AppProductOauthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * oauth协议应用扩展表(AppProductOauth)表服务实现类
 *
 * @author makejava
 * @since 2021-03-01 11:04:01
 */
@Service("appProductOauthService")
public class AppProductOauthServiceImpl implements AppProductOauthService {
    @Resource
    private AppProductOauthDao appProductOauthDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppProductOauth queryById(Object id) {
        return this.appProductOauthDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppProductOauth> queryAllByLimit(int offset, int limit) {
        return this.appProductOauthDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appProductOauth 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductOauth insert(AppProductOauth appProductOauth) {
        this.appProductOauthDao.insert(appProductOauth);
        return appProductOauth;
    }

    /**
     * 修改数据
     *
     * @param appProductOauth 实例对象
     * @return 实例对象
     */
    @Override
    public AppProductOauth update(AppProductOauth appProductOauth) {
        this.appProductOauthDao.update(appProductOauth);
        return this.queryById(appProductOauth.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Object id) {
        return this.appProductOauthDao.deleteById(id) > 0;
    }
}