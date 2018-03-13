package com.taoding.domain.customerTaxItem;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.entity.DataEntity;

/**
 * 客户税项设置表Entity
 * @author mhb
 * @version 2017-11-22
 */
@Data
public class CustomerTaxItem extends DataEntity<CustomerTaxItem> {
	/**
	 * 1:月计提 2: 季度计提
	 */
	public static final byte  MONTH_ACCRUED_TYPE=1;   //月计提
	public static final byte  SEASON_ACCRUED_TYPE=2;  //季度计提
	/**
	 * 1:小规模纳税人 2:一般规模纳税人
	 */
	public static final String SMALL_SCALE_TAXPAYER="1";  // 小规模纳税人    
	public static final String GENERAL_TAXPAYER="2";   //一般规模纳税人
	/**
	 * true:啟用  false:禁用
	 */
	public static final boolean  DEL_EABLE_NORMAL=true;
	public static final boolean DEL_EABLE_DELETE=false;
	@NotNull(message="税项名称不能为空")
	@Length(min=1, max=100, message="税项名称长度必须介于 1 和 200 之间")
	private String name;  //税项名称
	private Double rate;  //税率
	private boolean deleteable;  //可否删除
	private boolean  enable;    //是否启用
	@NotNull(message="账薄信息不能为空")
	@Length(min=1, max=64, message="账薄长度必须介于 1 和 64 之间")
	private String accountingBookId;//账薄id
	@NotNull(message="科目信息不能为空")
	@Length(min=1, max=64, message="科目长度必须介于 1 和 64 之间")
	private String subjectId;//科目id
	private byte accruedType;//计提方式(1:月计提 2 季度计提)
	/*
	 * 扩充字段
	 * */
	private String subjectName;   //科目名称

}
