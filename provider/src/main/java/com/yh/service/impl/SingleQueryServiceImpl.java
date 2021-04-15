package com.yh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yh.dao.MenuPermissionDao;
import com.yh.dao.RelationRoleMenuPermissionDao;
import com.yh.dao.SingleQueryDao;
import com.yh.entity.MenuInfo;
import com.yh.entity.MenuPermission;
import com.yh.entity.RelationRoleMenuPermission;
import com.yh.service.SingleQueryService;
import com.yh.view.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SingleQueryServiceImpl implements SingleQueryService {

    @Resource
    SingleQueryDao singleQueryDao;

    @Resource
    RelationRoleMenuPermissionDao relationRoleMenuPermissionDao;

    @Resource
    MenuPermissionDao menuPermissionDao;


    @Override
    public List<MenuVO> menus() {
        return singleQueryDao.menus();
    }

    @Override
    public List<MenuInfo> findByAppCodeAndParentId(String code, String parentId) {
        log.info("根据应用编码&父级编码&删除状态查找菜单信息：{},{},{}",code,parentId);
        QueryWrapper<MenuInfo> params = new QueryWrapper<>();
        params.eq("business_type",code);
        params.eq("parent_id",parentId);
        /*params.eq("is_delete",0);*/

        List<MenuInfo> result = singleQueryDao.selectList(params);
        if (!CollectionUtils.isEmpty(result)){
            return  new ArrayList<>();
        }
        return result;
    }

    @Override
    public MenuInfo findById(String id) {
        log.info("根据id查找菜单信息：{},{},{}",id);
        QueryWrapper<MenuInfo> params = new QueryWrapper<>();
        params.eq("id",id);
        /*params.eq("is_delete",0);*/
        return singleQueryDao.selectOne(params);
    }

    @Override
    public List<MenuInfo> findMenuByIds(String id){
        log.info("根据id查找菜单信息：{},{},{}",id);
        List<String> ids = Arrays.asList(id.split(","));
        QueryWrapper<MenuInfo> params = new QueryWrapper<>();
        params.in("id",ids);
       /* params.eq("is_delete",0);*/
        return singleQueryDao.selectList(params);
    }

    @Override
    public List<RelationRoleMenuPermission> queryRelationRoleMenuPermission(List<String> roleIds) {
        QueryWrapper<RelationRoleMenuPermission> wrapper = new QueryWrapper<>();
        wrapper.in("role_id",roleIds);
        /*wrapper.eq("is_delete",0);*/
        return relationRoleMenuPermissionDao.selectList(wrapper);
    }

    @Override
    public List<String> findProductCodes(List<Integer> menuPermissionIds) {
        QueryWrapper<MenuPermission> wrapper = new QueryWrapper();
        wrapper.in("id",menuPermissionIds);
        /*wrapper.eq("is_delete",0);*/
        List<MenuPermission> menuPermissions = menuPermissionDao.selectList(wrapper);
        if (CollectionUtils.isEmpty(menuPermissions)){
           return Collections.EMPTY_LIST;
        }
        List<String> productCodes = menuPermissions.stream().map(MenuPermission::getBusinessType).distinct().collect(Collectors.toList());
        return productCodes;
    }

    @Override
    public MenuPermission findByMenuPermissionId(String menuPermissionId) {
        QueryWrapper<MenuPermission> wrapper = new QueryWrapper();
        wrapper.in("id",menuPermissionId);
       /* wrapper.eq("is_delete",0);*/
        return menuPermissionDao.selectOne(wrapper);
    }

    @Override
    public List<MenuPermission> findByMenuPermissionIds(String menuPermissionId){
        if (StringUtils.isBlank(menuPermissionId)){
            return new ArrayList<>();
        }
        List<String> ids = Arrays.asList(menuPermissionId.split(","));
        QueryWrapper<MenuPermission> wrapper = new QueryWrapper();
        wrapper.in("id",ids);
        return menuPermissionDao.selectList(wrapper);
    }
}
