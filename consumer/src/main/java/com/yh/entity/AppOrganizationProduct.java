package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织架构-应用表(AppOrganizationProduct)实体类
 *
 * @author makejava
 * @since 2021-03-01 14:15:55
 */
@Data
public class AppOrganizationProduct implements Serializable {

    private static final long serialVersionUID = 285588091341695200L;

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
     * 组织架构code
     */
    private String organizationCode;
    /**
     * 组织架构name
     */
    private String organizationName;
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