package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (UserInfo)实体类
 *
 * @author makejava
 * @since 2021-03-04 15:04:09
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 642443833046921333L;

    private String id;
    /**
     * 工号
     */
    private String userNumber;
    /**
     * 手机号
     */
    private String telephone;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐
     */
    private String passwordSalt;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像地址
     */
    private String avatarUrl;
    /**
     * 性别 1男 2女
     */
    private Integer gender;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态 1可用 2禁用
     */
    private Integer status;
    /**
     * 用户登录token
     */
    private String token;
    /**
     * 最后一次登录时间
     */
    private Date lastLoginTime;
    /**
     * 系统来源
     */
    private String userSource;
    /**
     * 0:正常 1:删除
     */
    private Integer isDelete;

    private Date createdTime;

    private Date updatedTime;

    private String createdBy;

    private String updatedBy;
    /**
     * 职位代码
     */
    private String posPositionCode;
    /**
     * 职位名称
     */
    private String posPositionName;
    /**
     * 职务代码
     */
    private String posTitleId;
    /**
     * 职务名称
     */
    private String posTitleName;
    /**
     * 人员所在组织ID
     */
    private String orgunitCode;

    private Integer roleCount;

    private Integer purchesGroupCount;

    private Integer addressCount;

    private Integer plateCount;

    private Integer companyCount;

    private Integer profitCount;

    private Integer archiveModelCount;

    private Integer archiveCount;

    private Integer organizationCount;

    private Integer budgetCount;

    /**
     * 采购中台数据迁移标识
     */
    private Integer operateFlag;
    /**
     * 用户中心 回写字段
     */
    private String userCode;
    /**
     * 0:未关联数据权限 1:已关联数据权限
     */
    private Integer dataFlag;

    public UserInfo() {

    }

    public UserInfo(String id, String userNumber, String email, String nickname, String name, Integer gender, Integer status) {
        this.id = id;
        this.userNumber = userNumber;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.status = status;
    }
}