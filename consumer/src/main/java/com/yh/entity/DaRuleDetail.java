package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （规则明细）(DaRuleDetail)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:31:21
 */
@Data
public class DaRuleDetail implements Serializable {

    private static final long serialVersionUID = 238894292475844722L;

    private Long id;
    /**
     * （应用编码）
     */
    private String productCode;
    /**
     * （规则id）
     */
    private Integer ruleId;
    /**
     * （规则项目id）
     */
    private Integer projectId;

    private String projectName;
    /**
     * （操作符号）
     */
    private String operateMark;
    /**
     * （逻辑符号）
     */
    private String logicMark;
    /**
     * （值）
     */
    private String value;
    /**
     * （序号）
     */
    private Integer sort;
    /**
     * （前置符号）
     */
    private String beforeMark;
    /**
     * （后置符号）
     */
    private String afterMark;

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