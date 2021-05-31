package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * (RelationUserRole)实体类
 *
 * @author makejava
 * @since 2021-03-04 14:25:12
 */
@Data
public class RelationUserRole implements Serializable {
    private static final long serialVersionUID = 791941970867657024L;

    private BigInteger id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 0:正常 1:删除
     */
    private Integer isDelete;

    private Date createdTime;

    private Date updatedTime;

    private String createdBy;

    private String updatedBy;
}