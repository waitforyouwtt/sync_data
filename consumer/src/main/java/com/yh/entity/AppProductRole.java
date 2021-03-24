package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用角色表(AppProductRole)实体类
 *
 * @author makejava
 * @since 2021-02-25 11:28:26
 */
@Data
public class AppProductRole implements Serializable {

    private static final long serialVersionUID = -78249827005725695L;

    private Integer id;
    /**
     * 唯一code
     */
    private String uniqueCode;
    /**
     * 应用code
     */
    private String productCode;
    /**
     * 租户编码
     */
    private String tenantCode;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 状态 Y or N
     */
    private String roleStatus;
    /**
     * 外部角色编码
     */
    private String outRoleCode;
    /**
     * 平台
     */
    private String platform;
    /**
     *扩展字段：父级编码
     */
    private String extension1;
    /**
     *扩展字段：path
     */
    private String extension2;
    /**
     * 扩展字段：
     */
    private String extension3;
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