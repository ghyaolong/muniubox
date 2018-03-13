package com.taoding.domain.customerInfo;



import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Email;

import com.taoding.common.entity.DataEntity;

/**
 * 客户联系人Entity
 * @author mhb
 * @version 2017-11-16
 */
@Data
public class CpaCustomerLinkman extends DataEntity<CpaCustomerLinkman> {
	
	private static final long serialVersionUID = 1L;
	@NotNull(message="姓名为空")
	private String name;		// name
	private String position;		// position
	@Email(message="邮箱格式不正确")
	private String email;		// email
	private String phone;		// phone
	@NotNull(message="职位状态为空!")
	private char status;		// 1: 离职            0: 在职
	@NotNull(message="性别状态为空!")
	private char sex;		//  1 男  2 女
	@NotNull(message="客户信息不能为空")
	private String customerInfoId;  //客户id
	public CpaCustomerLinkman() {
		super();
	}

	public CpaCustomerLinkman(String id){
		super(id);
	}

	 
	
}