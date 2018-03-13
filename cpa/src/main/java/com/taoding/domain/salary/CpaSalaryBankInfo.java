package com.taoding.domain.salary;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.assisting.CpaAssistingEmployee;


/**
 * 客户的薪酬银行模板
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryBankInfo extends DataEntity<CpaSalaryBankInfo>{

	@Length(min=1, max=64)
	private String customerId;  //客户id
	
	private int templateCode;   //工资代发模板编码
}
