package com.yh.entity.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuerySingleResource implements Serializable {

    private String productCode;

    private String resourceCode;

    private String resourceName;
}
