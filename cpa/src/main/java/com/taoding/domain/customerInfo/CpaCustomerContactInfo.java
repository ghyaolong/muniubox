package com.taoding.domain.customerInfo;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.entity.DataEntity;

/**
 * 客户签约信息表Entity
 * @author mhb
 * @version 2017-11-16
 */
@Data
public class CpaCustomerContactInfo extends DataEntity<CpaCustomerContactInfo> {
	
	private static final long serialVersionUID = 1L;
	private String customerId;		// customer_id
	private String contactNo;		// 合同编码
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date contactDate;		// 合同日期
	private String incomeStatus;		// 收款状态 
	
	public CpaCustomerContactInfo() {
		super();
	}

	public CpaCustomerContactInfo(String id){
		super(id);
	}
}