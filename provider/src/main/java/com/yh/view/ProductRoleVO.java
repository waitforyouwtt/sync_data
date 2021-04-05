package com.yh.view;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductRoleVO implements Serializable {

    private String productCode;

    private Long roleId;
}
