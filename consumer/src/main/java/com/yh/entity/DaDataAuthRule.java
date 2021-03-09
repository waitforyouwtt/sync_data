package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （数据权限和规则关联表）(DaDataAuthRule)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:30:09
 */
@Data
public class DaDataAuthRule implements Serializable {
    private static final long serialVersionUID = 416019939488919429L;

    private Long id;
    /**
     * （应用编码）
     */
    private String productCode;
    /**
     * （数据权限id）
     */
    private Long dataAuthId;
    /**
     * （数据规则id）
     */
    private Long dataRuleId;
    /**
     * （数据规则名称）
     */
    private String dataRuleName;
    /**
     * （数据规则编码）
     */
    private String dataRuleCode;
    /**
     * （数据规则类型）
     */
    private String dataRuleType;

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