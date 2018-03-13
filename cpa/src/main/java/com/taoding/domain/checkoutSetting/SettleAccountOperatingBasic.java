package com.taoding.domain.checkoutSetting;

import java.math.BigDecimal;

import lombok.Data;

import com.taoding.common.entity.DataEntity;
/**
 * 经营数据分析基础表Entity
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Data
public class SettleAccountOperatingBasic extends DataEntity<SettleAccountOperatingBasic> {
	
	private String subOrder;  //结账分项键
	private String subName; //结账分项名称
	private Integer basicType;  //类型 1 基础 2 其他异常
	private BigDecimal startNum; //开始数值
	private BigDecimal endNum; //结束数值

}
