package com.yh.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.AppProductRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 应用角色表(AppProductRole)表数据库访问层
 *
 * @author makejava
 * @since 2021-02-25 11:28:26
 */
@Mapper
public interface AppProductRoleDao extends BaseMapper<AppProductRole> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    AppProductRole queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<AppProductRole> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param appProductRole 实例对象
     * @return 对象列表
     */
    List<AppProductRole> queryAll(AppProductRole appProductRole);

    /**
     * 新增数据
     *
     * @param appProductRole 实例对象
     * @return 影响行数
     */
    int insert(AppProductRole appProductRole);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductRole> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<AppProductRole> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<AppProductRole> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<AppProductRole> entities);

    /**
     * 修改数据
     *
     * @param appProductRole 实例对象
     * @return 影响行数
     */
    int update(AppProductRole appProductRole);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<AppProductRole> queryAll2();
}