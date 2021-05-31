package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * (RelationRoleMenuPermission)实体类
 *
 * @author makejava
 * @since 2021-02-25 14:48:36
 */
@Data
public class RelationRoleMenuPermission implements Serializable {
    private static final long serialVersionUID = 794959371908528579L;

    private BigInteger id;
    /**
     * 角色id
     */
    private Long roleId;

    private Long menuPermissionId;
    /**
     * 0:正常 1:删除
     */
    private Integer isDelete;

    private Date createdTime;

    private Date updatedTime;

    private String createdBy;

    private String updatedBy;
}