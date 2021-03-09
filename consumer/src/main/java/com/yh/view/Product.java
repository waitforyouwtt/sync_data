package com.yh.view;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {

    private String productCode;

    private String productName;
}
