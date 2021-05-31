package com.yh.service.impl;

import com.yh.dao.RelationUserRoleDao;
import com.yh.entity.RelationUserRole;
import com.yh.service.RelationUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * (RelationUserRole)表服务实现类
 *
 * @author makejava
 * @since 2021-03-04 14:25:13
 */
@Service("relationUserRoleService")
public class RelationUserRoleServiceImpl implements RelationUserRoleService {
    @Resource
    private RelationUserRoleDao relationUserRoleDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RelationUserRole queryById(BigInteger id) {
        return this.relationUserRoleDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<RelationUserRole> queryAllByLimit(int offset, int limit) {
        return this.relationUserRoleDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param relationUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public RelationUserRole insert(RelationUserRole relationUserRole) {
        this.relationUserRoleDao.insert(relationUserRole);
        return relationUserRole;
    }

    /**
     * 修改数据
     *
     * @param relationUserRole 实例对象
     * @return 实例对象
     */
    @Override
    public RelationUserRole update(RelationUserRole relationUserRole) {
        this.relationUserRoleDao.update(relationUserRole);
        return this.queryById(relationUserRole.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.relationUserRoleDao.deleteById(id) > 0;
    }

    @Override
    public List<RelationUserRole> findRelationUserRoles(Long min, Long max) {
        return relationUserRoleDao.queryBetweenId(min,max);
    }

    @Override
    public List<Integer> findIds(Long min, Long max) {
        return relationUserRoleDao.queryIds(min,max);
    }

    @Override
    public List<Long> findCountRelationUserRoles() {
        return relationUserRoleDao.findCountRelationUserRoles();
    }
}