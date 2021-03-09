package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * （规则项目）(DaRuleProject)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:31:43
 */
@Data
public class DaRuleProject implements Serializable {

    private static final long serialVersionUID = -68536386311727539L;
    /**
     * （项目id）
     */
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
     * （对象属性名）
     */
    private String attribName;
    /**
     * （值类型）
     */
    private String valType;
    /**
     * （UI类型）
     */
    private String uiType;
    /**
     * （默认值）
     */
    private String defVal;
    /**
     * （格式）
     */
    private String form;

    private String selectList;

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