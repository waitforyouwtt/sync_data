package com.yh.service;

import com.yh.entity.AppProduct;

import java.util.List;

/**
 * 应用表(AppProduct)表服务接口
 *
 * @author makejava
 * @since 2021-03-01 11:03:05
 */
public interface AppProductService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProduct queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProduct> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param appProduct 实例对象
     * @return 实例对象
     */
    AppProduct insert(AppProduct appProduct);

    /**
     * 修改数据
     *
     * @param appProduct 实例对象
     * @return 实例对象
     */
    AppProduct update(AppProduct appProduct);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Object id);

}