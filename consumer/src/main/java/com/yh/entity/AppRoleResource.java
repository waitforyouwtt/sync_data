package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色资源表(AppRoleResource)实体类
 *
 * @author makejava
 * @since 2021-02-25 17:45:26
 */
@Data
public class AppRoleResource implements Serializable {
    private static final long serialVersionUID = 698833792433801877L;

    private Integer id;
    /**
     * 应用code
     */
    private String productCode;
    /**
     * 应用名称
     */
    private String productName;
    /**
     * 租户code
     */
    private String tenantCode;
    /**
     * 角色编号
     */
    private String roleCode;
    /**
     * 资源code
     */
    private String resourceCode;
    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 平台
     */
    private String platform;

    /**
     * 删除标记
     */
    private Integer isDelete;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 修改人
     */
    private String updatedBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 修改时间
     */
    private Date updatedTime;
}