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
     * 角色按应用拆分后进行同步
     * @return
     */
    String roleSplitByApplication();

    /**
     * 同步丢弃但已经绑定用户的角色
     * @return
     */
    String syncAbandonList();

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
