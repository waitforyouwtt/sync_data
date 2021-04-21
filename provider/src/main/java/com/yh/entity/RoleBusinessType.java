package com.yh.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author chensong
 * Date on  2021/4/17
 */
@Data
public class RoleBusinessType implements Serializable {
    private BigInteger roleId;
    private String businessType;
}
