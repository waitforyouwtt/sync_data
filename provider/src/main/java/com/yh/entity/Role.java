package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Role implements Serializable {

    /**
     * 角色表主键id
     */
    private BigInteger roleId;

    /**
     * 假的哦，存储在身份治理的第三方role_code
     */
    private String roleCode;

    private String roleName;

    /**
     * 存放ADMIN
     */
    private String roleTypeCode;

    /**
     * 父级编码
     */
    private BigInteger parentId;

    /**
     *
     */
    private String rolePath;
}
