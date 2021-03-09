package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-角色表(AppUserRole)实体类
 *
 * @author makejava
 * @since 2021-03-04 14:59:07
 */
@Data
public class AppUserRole implements Serializable {

    private static final long serialVersionUID = -50773746445636847L;

    private Integer id;
    /**
     * 应用code
     */
    private String productCode;
    /**
     * 租户code
     */
    private String tenantCode;
    /**
     * 用户id
     */
    private String userCode;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
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