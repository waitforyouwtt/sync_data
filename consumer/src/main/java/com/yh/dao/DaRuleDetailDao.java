package com.yh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yh.entity.DaRuleDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * （规则明细）(DaRuleDetail)表数据库访问层
 *
 * @author makejava
 * @since 2021-03-03 16:31:21
 */
@Mapper
public interface DaRuleDetailDao extends BaseMapper<DaRuleDetail> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DaRuleDetail queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<DaRuleDetail> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param daRuleDetail 实例对象
     * @return 对象列表
     */
    List<DaRuleDetail> queryAll(DaRuleDetail daRuleDetail);

    /**
     * 新增数据
     *
     * @param daRuleDetail 实例对象
     * @return 影响行数
     */
    int insert(DaRuleDetail daRuleDetail);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRuleDetail> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DaRuleDetail> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DaRuleDetail> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<DaRuleDetail> entities);

    /**
     * 修改数据
     *
     * @param daRuleDetail 实例对象
     * @return 影响行数
     */
    int update(DaRuleDetail daRuleDetail);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}