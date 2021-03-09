package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.DaDataAuth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * （数据权限）(DaDataAuth)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-03 16:28:58
 */
@Mapper
public interface DaDataAuthDao extends BaseMapper<DaDataAuth> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DaDataAuth queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DaDataAuth> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param daDataAuth 实例对象
     * @return 对象列表
     */
    List<DaDataAuth> queryAll(DaDataAuth daDataAuth);

    /**
     * 新增数据
     *
     * @param daDataAuth 实例对象
     * @return 影响行数
     */
    int insert(DaDataAuth daDataAuth);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaDataAuth> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DaDataAuth> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaDataAuth> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DaDataAuth> entities);

    /**
     * 修改数据
     *
     * @param daDataAuth 实例对象
     * @return 影响行数
     */
    int update(DaDataAuth daDataAuth);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}