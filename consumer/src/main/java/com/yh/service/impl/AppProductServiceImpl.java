package com.yh.service.impl;

import com.yh.dao.AppProductDao;
import com.yh.entity.AppProduct;
import com.yh.service.AppProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用表(AppProduct)表服务实现类
 *
 * @author makejava
 * @since 2021-03-01 11:03:05
 */
@Service("appProductService")
public class AppProductServiceImpl implements AppProductService {
    @Resource
    private AppProductDao appProductDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AppProduct queryById(Integer id) {
        return this.appProductDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<AppProduct> queryAllByLimit(int offset, int limit) {
        return this.appProductDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param appProduct 实例对象
     * @return 实例对象
     */
    @Override
    public AppProduct insert(AppProduct appProduct) {
        this.appProductDao.insert(appProduct);
        return appProduct;
    }

    /**
     * 修改数据
     *
     * @param appProduct 实例对象
     * @return 实例对象
     */
    @Override
    public AppProduct update(AppProduct appProduct) {
        this.appProductDao.update(appProduct);
        return this.queryById(appProduct.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Object id) {
        return this.appProductDao.deleteById(id) > 0;
    }
}