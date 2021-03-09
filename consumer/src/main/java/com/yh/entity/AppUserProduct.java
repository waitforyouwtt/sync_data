package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-应用表(AppUserProduct)实体类
 *
 * @author makejava
 * @since 2021-03-01 19:08:03
 */
@Data
public class AppUserProduct implements Serializable {

    private static final long serialVersionUID = 599545199607402983L;

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
     * 租户名称
     */
    private String tenantName;
    /**
     * 用户编号
     */
    private String userCode;
    /**
     * 用户名字
     */
    private String userName;
    /**
     * 应用申请审批状态：0 等待审批 1，通过 2，拒绝'
     */
    private Integer status;
    /**
     * 数据所属组织
     */
    private String belongOrganizationCode;
    /**
     * 数据来源：1 用户授权 2 组织授权同步用户数据
     */
    private Integer dataSources;
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

    private Long userInfoId;
}