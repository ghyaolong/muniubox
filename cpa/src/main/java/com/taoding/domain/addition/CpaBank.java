package com.taoding.domain.addition;

import lombok.Data;
import lombok.ToString;

import org.hibernate.validator.constraints.NotEmpty;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.customerInfo.CustomerInfo;

/**
 * 银行信息Entity
 * 
 * @author mhb
 * @version 2017-11-16
 */
@Data
@ToString
@ValidationBean
public class CpaBank extends DataEntity<CpaBank> {

	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String bankName;    //银行名称
}