package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用表(AppProduct)实体类
 *
 * @author makejava
 * @since 2021-03-01 11:03:04
 */
@Data
public class AppProduct implements Serializable {

    private static final long serialVersionUID = -95435967117003229L;
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 应用唯一码
     */
    private String productCode;
    /**
     * 应用名称
     */
    private String productName;
    /**
     * 使用协议 :1  jwt 2 oauth 3 cas
     */
    private String protocol;
    /**
     * logo图片地址
     */
    private String logoUrl;
    /**
     * 应用跳转地址
     */
    private String redirectUrl;

    private String loginoutUrl;
    /**
     * 应用类型：1 web 应用 2 移动应用 3 PC客户端
     */
    private String productType;
    /**
     * 应用启用状态: Y 正常 N 禁用
     */
    private String productStatus;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 删除标记：0 正常 1 删除
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