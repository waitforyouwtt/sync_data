package com.yh.service.impl;

import com.yh.dao.RoleInfoDao;
import com.yh.entity.RoleInfo;
import com.yh.service.RoleInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (RoleInfo)表服务实现类
 *
 * @author makejava
 * @since 2021-02-25 10:17:04
 */
@Service("roleInfoService")
public class RoleInfoServiceImpl implements RoleInfoService {

    @Resource
    private RoleInfoDao roleInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RoleInfo queryById(Long id) {
        return this.roleInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<RoleInfo> queryAllByLimit(int offset, int limit) {
        return this.roleInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param roleInfo 实例对象
     * @return 实例对象
     */
    @Override
    public RoleInfo insert(RoleInfo roleInfo) {
        this.roleInfoDao.insert(roleInfo);
        return roleInfo;
    }

    /**
     * 修改数据
     *
     * @param roleInfo 实例对象
     * @return 实例对象
     */
    @Override
    public RoleInfo update(RoleInfo roleInfo) {
        this.roleInfoDao.update(roleInfo);
        return this.queryById(roleInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.roleInfoDao.deleteById(id) > 0;
    }

    @Override
    public List<RoleInfo> findRoleInfos() {
        RoleInfo info = new RoleInfo();
        info.setIsDelete(0);
        return this.roleInfoDao.queryAll(info);
    }

    @Override
    public List<String> findCountRoles() {
        return roleInfoDao.findCountRoles();
    }

    @Override
    public List<RoleInfo> findRoleBetweenIds(Integer min, Integer max) {
        return roleInfoDao.findRoleBetweenIds(min,max);
    }
}