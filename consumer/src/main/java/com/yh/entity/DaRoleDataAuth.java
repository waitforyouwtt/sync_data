package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （角色和数据权限关联表）(DaRoleDataAuth)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:30:55
 */
@Data
public class DaRoleDataAuth implements Serializable {
    private static final long serialVersionUID = -48214890844613807L;

    private Long id;
    /**
     * （应用编码）
     */
    private String productCode;
    /**
     * （角色id）
     */
    private Long roleId;

    private String roleCode;
    /**
     * （数据权限id）
     */
    private Long dataAuthId;

    private String dataAuthName;

    private String dataAuthCode;

    private String tenantCode;
    /**
     * （是否删除）
     */
    private Integer isDelete;
    /**
     * （创建人）
     */
    private String createdBy;
    /**
     * （更新人）
     */
    private String updatedBy;
    /**
     * （创建时间）
     */
    private Date createdTime;
    /**
     * （更新时间）
     */
    private Date updatedTime;
}