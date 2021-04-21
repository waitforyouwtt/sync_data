package com.yh.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (ConfigResource)实体类
 *
 * @author makejava
 * @since 2021-04-17 13:44:17
 */
@Data
public class ConfigResource implements Serializable {

    private static final long serialVersionUID = 177016695598796597L;
    
    private Integer id;

    /**
    * 应用名字
    */
    private String code;
    
    private String name;
    
    private String exitUrl;
    
    private String redirectUrl;

    private String clientId;

    private String clientSecret;

    /**
     *  应用类型：1 web 应用 2 移动应用 3 PC客户端
     */
    private String productType;

    private Integer isDelete;
}