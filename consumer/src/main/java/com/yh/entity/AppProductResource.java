package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用资源表(AppProductResource)实体类
 *
 * @author makejava
 * @since 2021-02-24 10:35:38
 */
@Data
public class AppProductResource implements Serializable {
    private static final long serialVersionUID = -56886654913447081L;

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
     * 租户code
     */
    private String tenantCode;
    /**
     * 父级
     */
    private String parentCode;
    /**
     * 资源编码
     */
    private String resourceCode;
    /**
     * 名称
     */
    private String resourceName;
    /**
     * 资源路径
     */
    private String path;
    /**
     * 类型:菜单，按钮，api
     */
    private String type;
    /**
     * 资源请求类型：1 get 2 post 3 put 4 delete
     */
    private String resourceType;
    /**
     * 排序
     */
    private Integer orderNum;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 扩展字段1
     */
    private String expand1;
    /**
     * 扩展字段2：存放菜单code
     */
    private String expand2;
    /**
     * 扩展字段3：存放菜单父级id
     */
    private String expand3;
    /**
     * 扩展字段4
     */
    private String expand4;
    /**
     * 扩展字段5,json字段
     */
    private String expand5;

    private String status;

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