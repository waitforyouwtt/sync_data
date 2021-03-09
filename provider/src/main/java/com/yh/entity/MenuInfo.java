package com.yh.entity;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * (MenuInfo)实体类
 *
 * @author makejava
 * @since 2021-02-22 17:53:45
 */
@Data
@Table(name ="menu_info")
public class MenuInfo implements Serializable {

    private static final long serialVersionUID = -73500409665383268L;

    private Integer id;
    /**
     * 类型：1页面 2其他
     */
    private Integer type;

    private Integer parentId;
    /**
     * 编码
     */
    private String code;
    /**
     * 操作对象名称
     */
    private String name;
    /**
     * 显示的url地址
     */
    private String displayUrl;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 序列号
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
    /**
     * 门店.商行.后台等业务类型
     */
    private String businessType;
    /**
     * 状态: 0 禁用 1可用
     */
    private Integer status;

    private Integer outMenuId;

}