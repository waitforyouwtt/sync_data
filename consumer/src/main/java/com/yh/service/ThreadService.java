package com.yh.service;

public interface ThreadService {

    /**
     * 菜单同步资源
     * @return
     */
    String menuSyncResource();

    /**
     * 按钮同步资源
     * @return
     */
    String menuPermissionSyncResource();

    /**
     * 角色同步
     * @return
     */
    String roleSync();

    /**
     * 角色资源同步
     * @return
     */
    String roleResource();

    /**
     * 用户角色关联同步
     * @return
     */
    String syncRelationUserRoles();

    /**
     * 应用同步
     * @return
     */
    String syncApplication();
}
