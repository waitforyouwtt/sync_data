package com.yh.service;

public interface SyncService {

    /**
     * 菜单同步
     * @return
     */
    String menuSyncResource();

    /**
     * 菜单按钮同步
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
    String roleResource(Long min,Long max);

    /**
     * 角色用户
     * @param min
     * @param max
     * @return
     */
    String syncRelationUserRoles(Long min, Long max);

    String syncRelationUserRoles3(Long min, Long max);

    /**
     * 同步租户信息
     * @return
     */
    String syncTenant();
}
