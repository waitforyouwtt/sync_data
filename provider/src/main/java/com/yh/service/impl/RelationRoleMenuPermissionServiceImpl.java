package com.yh.service.impl;

import com.yh.dao.RelationRoleMenuPermissionDao;
import com.yh.entity.RelationRoleMenuPermission;
import com.yh.service.RelationRoleMenuPermissionService;
import com.yh.service.SingleQueryService;
import com.yh.view.ProductRoleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (RelationRoleMenuPermission)表服务实现类
 *
 * @author makejava
 * @since 2021-02-25 14:48:37
 */
@Service("relationRoleMenuPermissionService")
public class RelationRoleMenuPermissionServiceImpl implements RelationRoleMenuPermissionService {
    @Resource
    private RelationRoleMenuPermissionDao relationRoleMenuPermissionDao;

    @Autowired
    private SingleQueryService singleQueryService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public RelationRoleMenuPermission queryById(Integer id) {
        return this.relationRoleMenuPermissionDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<RelationRoleMenuPermission> queryAllByLimit(int offset, int limit) {
        return this.relationRoleMenuPermissionDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 实例对象
     */
    @Override
    public RelationRoleMenuPermission insert(RelationRoleMenuPermission relationRoleMenuPermission) {
        this.relationRoleMenuPermissionDao.insert(relationRoleMenuPermission);
        return relationRoleMenuPermission;
    }

    /**
     * 修改数据
     *
     * @param relationRoleMenuPermission 实例对象
     * @return 实例对象
     */
    @Override
    public RelationRoleMenuPermission update(RelationRoleMenuPermission relationRoleMenuPermission) {
        this.relationRoleMenuPermissionDao.update(relationRoleMenuPermission);
        return this.queryById(relationRoleMenuPermission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.relationRoleMenuPermissionDao.deleteById(id) > 0;
    }

    @Override
    public List<String> findProductCodeByRoleId(String roleId) {
        List<String> roleCodes = Arrays.asList(roleId.split(","));
        List<RelationRoleMenuPermission> relationRoleMenuPermissions = this.singleQueryService.queryRelationRoleMenuPermission(roleCodes);
        if (CollectionUtils.isEmpty(relationRoleMenuPermissions)){
            return Collections.EMPTY_LIST;
        }
        List<Integer> menuPermissionIds = relationRoleMenuPermissions.stream().map(RelationRoleMenuPermission::getMenuPermissionId).collect(Collectors.toList());
        List<String> productCodes = this.singleQueryService.findProductCodes(menuPermissionIds);
        return productCodes;
    }

    @Override
    public List<ProductRoleVO> findProductCodeByRoleIds(String roleIds) {
        if (StringUtils.isBlank(roleIds)){
            return new ArrayList<>();
        }
        List<String> roleCodes = Arrays.asList(roleIds.split(","));
        List<RelationRoleMenuPermission> relationRoleMenuPermissions = this.singleQueryService.queryRelationRoleMenuPermission(roleCodes);
        if (CollectionUtils.isEmpty(relationRoleMenuPermissions)){
            return Collections.EMPTY_LIST;
        }
        Map<Integer, List<RelationRoleMenuPermission>> integerListMap = relationRoleMenuPermissions.stream().collect(Collectors.groupingBy(RelationRoleMenuPermission::getRoleId));
        List<ProductRoleVO> products = new ArrayList<>();
        integerListMap.forEach((k, v) -> {
            System.out.println("key:value = " + k + ":" + v);
            List<Integer> menuPermissionIds = v.stream().map(RelationRoleMenuPermission::getMenuPermissionId).distinct().collect(Collectors.toList());
            List<String>  productCodes = this.singleQueryService.findProductCodes(menuPermissionIds);
            ProductRoleVO vo = new ProductRoleVO();
            vo.setRoleId(Long.valueOf(k));
            vo.setProductCode(String.join(",",productCodes));
            products.add(vo);
        });
        return products;
    }

    private List<ProductRoleVO> product(){
        return new ArrayList<>();
    }

    @Override
    public List<RelationRoleMenuPermission> findRelationRoleMenuPermissions(Integer min,Integer max) {
        return this.relationRoleMenuPermissionDao.queryBetweenId(min,max);
    }

    @Override
    public List<Integer> findCountRelationRoleMenuPermissions() {
        return relationRoleMenuPermissionDao.findCountRelationRoleMenuPermissions();
    }
}