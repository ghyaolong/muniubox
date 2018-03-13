package com.taoding.domain.checkoutSetting;

import lombok.Data;

import com.taoding.common.entity.DataEntity;
/**
 * 结账基础配置Entity
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Data
public class SettleAccountBasic extends DataEntity<SettleAccountBasic>{ 
	private String subOrder;  //结账分项键
	private String subName; //结账分项名称
	private Integer basicType;  //类型 1 基础 2 其他异常 
}
