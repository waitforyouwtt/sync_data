package com.yh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * response/request product parent info
 * 
 * @author xiejinhua
 * @date 2020-06-16
 *
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper=false)
public class ProductVO extends BaseEntity {

	@ApiModelProperty(value = "应用唯一码")
	private String productCode;

	@ApiModelProperty(value = "应用名称")
	private String productName;

	@ApiModelProperty(value = "图标地址")
	private String logoUrl;

	@ApiModelProperty(value = "应用跳转地址")
	private String redirectUrl;

	@ApiModelProperty(value = "应用登出地址")
	private String loginoutUrl;

	@ApiModelProperty(value = "应用类型:1 web 应用 2 移动应用 3 PC客户端")
	private String productType;

	@ApiModelProperty(value = "应用启用状态: Y 正常 N 禁用")
	private String productStatus;

	@ApiModelProperty(value = "应用使用协议 :1  jwt 2 oauth 3 cas")
	private String protocol;

	@ApiModelProperty(value = "描述信息")
	private String description;

	@ApiModelProperty(value = "删除标记")
	private Integer isDelete;

	@ApiModelProperty(value = "创建人")
	private String createdBy;

	@ApiModelProperty(value = "修改人")
	private String updatedBy;

	@ApiModelProperty(value = "租户编码")
	private String tenantCode;
}