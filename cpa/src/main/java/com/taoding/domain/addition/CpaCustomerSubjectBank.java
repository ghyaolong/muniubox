package com.taoding.domain.addition;

import lombok.Data;
import lombok.ToString;

import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.domain.subject.CpaSubject;

/**
 * 客户银行科目表Entity
 * 
 * @author mhb
 * @version 2017-11-16
 */
@Data
@ToString
@ValidationBean
public class CpaCustomerSubjectBank extends DataEntity<CpaCustomerSubjectBank> {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String bankAccount;      //银行账号
	@NotEmpty
	private String bankAddress;       //开户地址
	private CustomerInfo customerInfo;    //客户
	private CpaBank cpaBank;    //银行
	private CpaSubject cpaSubject;   //科目信息
	
}