package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (MenuPermission)实体类
 *
 * @author makejava
 * @since 2021-02-23 11:18:15
 */
@Data
public class MenuPermission implements Serializable {
    private static final long serialVersionUID = 603678035244737875L;

    private Integer id;

    private String businessType;
    /**
     * 操作对象id
     */
    private Integer operationObjectiveId;

    private String operationObjectiveName;

    private String apiUrl;
    /**
     * apiUrl的HttpMethod：GET,PUT,POST,DELETE等
     */
    private String apiMethod;
    /**
     * 权限code
     */
    private String permissionCode;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 排序
     */
    private Integer rank;
    /**
     * 0:正常 1:删除
     */
    private Integer isDelete;

    private Date createdTime;

    private Date updatedTime;

    private String createdBy;

    private String updatedBy;

    private Integer outMenuId;

    private Integer outMenuPermissionId;
}