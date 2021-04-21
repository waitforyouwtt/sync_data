package com.yh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * add/edit Oauth Product info
 * 
 * @author xiejinhua
 * @date 2020-06-16
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class OauthProductVO extends ProductVO {

	@ApiModelProperty(value = "应用跳转 URI")
	private String redirectUrl;

	@ApiModelProperty("客户端编码")
	private String clientId;

	@ApiModelProperty("客户端秘钥")
	private String clientSecret;

	@ApiModelProperty(value = "授权码模式 authorization code implicit")
	private String grantType;
	
	@ApiModelProperty(value = "Access_Token有效期")
	private Long tokenExpirationTime;
	
	@ApiModelProperty(value = "Refresh Token有效期")
	private Long refreshTokenExpirationTime;
	
	@ApiModelProperty("响应类型：code")
	private String responseTypes;
}
