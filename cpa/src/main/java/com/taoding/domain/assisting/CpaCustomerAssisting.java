
package com.taoding.domain.assisting;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 辅助核算类型Entity
 * @author csl
 * @version 2017-11-16
 */
@Data
@ToString
@ValidationBean
public class CpaCustomerAssisting extends DataEntity<CpaCustomerAssisting> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=40, message="辅助核算类型名称长度必须介于 1 和 40 之间")
	private String catalogName;		//辅助核算类型名称
	
	@Length(min=1, max=64, message="账薄id长度必须介于 1 和 64 之间")
	private String accountId; //账薄id
	
	@Length(min=1, max=64, message="辅助核算模板ID长度必须介于 1 和 64 之间")
	private String templateId; //辅助核算模板ID
	
	@Length(min=1, max=1, message="是否是自定义类型的分类长度必须介于 1 和 1 之间")
	private String isCustom;		// 是否是自定义类型的分类
	
	@Length(min=0, max=11, message="排序长度必须介于 0 和 11 之间")
	private String sort;		// 排序
	
	@Length(min=0, max=11, message="辅助核算类型标识必须介于1 和 1 之间")
	private String assistingInfoType;     //辅助核算类型标识
	
	private CpaCustomerAssistingInfo cpaCustomerAssistingInfo; //辅助核算实体
	
	//辅助核算类型分类的类型标识
	public static final String TYPE_CONSTOMER = "1"; // 客户  name
	public static final String TYPE_PROVIDER = "2";	//供应商  name
	public static final String TYPE_DEPART = "3";	//部门 departmentName
	public static final String TYPE_EMPLOYEE = "4";	//员工  name
	public static final String TYPE_GOODS = "5";	//存货  goodsName
	public static final String TYPE_PROJECT = "6";	//项目  positionName
	public static final String TYPE_PAYMENT = "7";	//第三方支付  name
	public static final String TYPE_POSITION = "8";	//职位   projectName
	public static final String TYPE_CUSTOM = "9";	//自定义  name
	
	
}