package com.yh.view;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProductRoleView implements Serializable {

    private Integer roleId;
    private List<String> productCodes;
}
