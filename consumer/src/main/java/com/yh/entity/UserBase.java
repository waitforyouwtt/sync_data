package com.yh.entity;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 最小化用户信息(UserBase)实体类
 *
 * @author makejava
 * @since 2021-03-27 17:00:58
 */
@Data
public class UserBase implements Serializable {
    private static final long serialVersionUID = 246170424834859782L;
    /**
    * 用户ID
    */
    private Integer id;
    /**
    * 用户编码
    */
    private String userCode;
    /**
    * 真实姓名
    */
    private String realName;
    /**
    * 手机号
    */
    private String phoneNum;
    /**
    * 自定义登录名
    */
    private String customLoginName;
    /**
    * 邮箱
    */
    private String email;
    /**
    * 身份证号
    */
    private String idNumber;
    /**
    * ID
    */
    private String idType;
    /**
    * 密码
    */
    private String password;
    /**
    * 加密类型
    */
    private String encodeType;
    /**
    * male 男 female 女 unkown 未知
    */
    private String gender;
    /**
    * 昵称
    */
    private String nickName;
    /**
    * 头像
    */
    private String headImage;
    /**
    * 生日
    */
    private Date birthDate;
    /**
    * 员工 empIn custom 自定义比如供应商
    */
    private Integer userType;
    /**
    * 是否员工
    */
    private Integer isEmployee;
    /**
    * 是否是同步的数据
    */
    private Integer isSyncData;
    
    private Integer isDelete;
    
    private String createBy;
    
    private String updateBy;
    
    private Date createTime;
    
    private Date updateTime;
    /**
    * 是否禁用
    */
    private Integer isDisable;
    /**
    * 是否更改了密码
    */
    private Integer isChangePwd;
    /**
    * 员工固定的组织编码
    */
    private String organizationCode;
    /**
    * 员工状态 on 在职 off 离值 
    */
    private Integer empStatus;
    /**
    * 员工类型 official 正式 assignment 外派
    */
    private String empType;
    /**
    * 密码更新时间
    */
    private Date updatePasswordTime;
    /**
    * 登录失效时间 临时账号用
    */
    private Date loginDeadTime;

    private Integer appCode;
    /**
    * 账户有效期
    */
    private Integer endTime;

    private String remark;
    /**
    * 冗余过渡字段_采购中台数据迁移 对应user_info.id
    */
    private Long userInfoId;

}