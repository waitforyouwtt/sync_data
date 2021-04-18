package com.yh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chensong
 * Date on  2021/4/17
 */
@Data
public class RoleBusinessType implements Serializable {
    private Integer roleId;
    private String businessType;
}
