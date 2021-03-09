package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户信息表(AppTenantInfo)实体类
 *
 * @author makejava
 * @since 2021-03-01 16:40:52
 */
@Data
public class AppTenantInfo implements Serializable {

    private static final long serialVersionUID = 939719135595102557L;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 应用code
     */
    private String productCode;
    /**
     * logo
     */
    private String logo;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：Y正常 N 禁用
     */
    private String status;
    /**
     * 删除状态：0正常 1 禁用
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