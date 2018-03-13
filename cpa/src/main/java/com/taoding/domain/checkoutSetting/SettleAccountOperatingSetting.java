package com.taoding.domain.checkoutSetting;

import java.math.BigDecimal;

import lombok.Data;

import com.taoding.common.entity.DataEntity;
/**
 * 经营数据分析设置表Entity
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Data
public class SettleAccountOperatingSetting extends DataEntity<SettleAccountOperatingSetting> {
	
	private String bookId; //账簿ID 
	private String customerId;//客户ID
	private String subOrder;  //结账分项键
	private String subName; //结账分项名称
	private BigDecimal startNum; //开始数值
	private BigDecimal endNum; //结束数值

}
