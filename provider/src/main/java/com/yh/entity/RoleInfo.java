package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (RoleInfo)实体类
 *
 * @author makejava
 * @since 2021-02-25 10:17:04
 */
@Data
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = -70765005377848795L;

    private Long id;
    /**
     * 角色类型 ADMIN MANGER等
     */
    private String roleTypeCode;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 是否叶子节点
     */
    private Integer isLeaf;
    /**
     * 上级角色的id
     */
    private Long parentId;
    /**
     * 0:正常 1:删除
     */
    private Integer isDelete;

    private Date createdTime;

    private Date updatedTime;

    private String createdBy;

    private String updatedBy;
    /**
     * 角色的层级
     */
    private String rolePath;
    /**
     * 1 门店    2商行   3后台
     */
    private String businessType;

    private Long outRoleId;

    private Long outRoleIdTest;
}