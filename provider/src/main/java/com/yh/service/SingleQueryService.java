package com.yh.service;

import com.yh.entity.MenuInfo;
import com.yh.entity.MenuPermission;
import com.yh.entity.RelationRoleMenuPermission;
import com.yh.view.MenuVO;

import java.util.List;

public interface SingleQueryService {

    List<MenuVO> menus();

    List<MenuInfo> findByAppCodeAndParentId(String code, String parentId);

    /**
     * 按照operation_objective_id查找父级菜单:
     * 根据MenuPermission的operation_objective_id{menu 的主键哦} 查找menu 信息
     * @param id
     * @return
     */
    MenuInfo findById(String id);

    List<MenuInfo> findMenuByIds(String ids);

    /**
     * 通过角色id 倒推productCode
     * @param roleIds
     * @return
     */
    List<RelationRoleMenuPermission> queryRelationRoleMenuPermission(List<String> roleIds);

    /**
     * 通过角色id 倒推productCode
     * @param menuPermissionIds
     * @return
     */
    List<String> findProductCodes (List<Long> menuPermissionIds);

    MenuPermission findByMenuPermissionId(String menuPermissionId);

    List<MenuPermission> findByMenuPermissionIds(String menuPermissionId);

}
