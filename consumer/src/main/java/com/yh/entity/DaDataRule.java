package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （数据规则）(DaDataRule)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:30:34
 */
@Data
public class DaDataRule implements Serializable {

    private static final long serialVersionUID = -20775145280269690L;

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
     * （编码）
     */
    private String code;
    /**
     * （类型）
     */
    private String type;
    /**
     * （是否启用）
     */
    private Integer used;

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