package com.taoding.domain.checkoutSetting;

import java.util.List;

import lombok.Data;

import com.taoding.common.entity.DataEntity;
/**
 * 企业结账启用禁用配置Entity
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Data
public class SettleAccountCustomer extends DataEntity<SettleAccountCustomer> {
	
	/**常规检测设置*/
	public static final int VALIDATION_TYPE_GENERAL=1;
	/**其他异常检测*/
	public static final int VALIDATION_TYPE_OTHER=2;
	
	/**启用*/ 
	public static final boolean TRUE =  true;
	/**禁用*/
	public static final boolean FALSE = false;
	
	public static final String  MANAGEMENT_DATA_VALIDATION="经营数据分析设置检测";
	private String bookId; //账簿ID 
	private String customerId;//客户ID
	private String subOrder;  //结账分项键
	private String subName; //结账分项名称
	private Integer basicType;  //类型 1 基础 2 其他异常
	private boolean enable;  //启用 禁用 
}
