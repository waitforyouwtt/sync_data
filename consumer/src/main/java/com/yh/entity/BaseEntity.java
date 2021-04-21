package com.yh.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class BaseEntity implements Serializable {

	@ApiModelProperty(value = "操作人编码")
	private String optCode;

	@ApiModelProperty(value = "操作人")
	private String optBy;
}
