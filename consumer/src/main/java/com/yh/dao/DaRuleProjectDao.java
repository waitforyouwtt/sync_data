package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.DaRuleProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * （规则项目）(DaRuleProject)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-03 16:31:43
 */
@Mapper
public interface DaRuleProjectDao extends BaseMapper<DaRuleProject> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DaRuleProject queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DaRuleProject> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param daRuleProject 实例对象
     * @return 对象列表
     */
    List<DaRuleProject> queryAll(DaRuleProject daRuleProject);

    /**
     * 新增数据
     *
     * @param daRuleProject 实例对象
     * @return 影响行数
     */
    int insert(DaRuleProject daRuleProject);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRuleProject> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DaRuleProject> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRuleProject> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DaRuleProject> entities);

    /**
     * 修改数据
     *
     * @param daRuleProject 实例对象
     * @return 影响行数
     */
    int update(DaRuleProject daRuleProject);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}