package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （数据权限）(DaDataAuth)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:28:57
 */
@Data
public class DaDataAuth implements Serializable {

    private static final long serialVersionUID = 321921567611104596L;

    private Long id;
    /**
     * （应用编码）
     */
    private String productCode;
    /**
     * （名称）
     */
    private String name;
    /**
     * （功能编码）
     */
    private String code;
    /**
     * （是否启用）
     */
    private Integer used;
    /**
     * （描述）
     */
    private String description;

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