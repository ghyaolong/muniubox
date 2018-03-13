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
  * 薪酬发放银行模板
 * @author fc
 *
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryBankTemplate extends DataEntity<CpaSalaryBankTemplate>{

	@Length(min=1, max=60)
	private String bankName;  //银行名称
	
	@Length(min=0, max=100)
	private String templateLocation;  //模板位置
	
	@Length(min=0, max=11)   //代码
	private int code;
}
