package com.yh.service;

import com.yh.entity.MenuInfo;
import com.yh.entity.MenuPermission;
import com.yh.entity.RelationRoleMenuPermission;
import com.yh.view.MenuVO;

import java.util.List;

public interface SingleQueryService {

    List<MenuVO> menus();

    List<MenuInfo> findByAppCodeAndParentId(String code, String parentId);

    MenuInfo findById(String id);

    List<RelationRoleMenuPermission> queryRelationRoleMenuPermission(String roleId);

    List<String> findProductCodes (List<Integer> menuPermissionIds);

    MenuPermission findByMenuPermissionId(String menuPermissionId);

}
