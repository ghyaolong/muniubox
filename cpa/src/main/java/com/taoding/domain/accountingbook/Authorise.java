package com.taoding.domain.accountingbook;


import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
/**
 * 
* @ClassName: Authorise 
* @Description: 
* @author lixc 
* @date 2017年11月21日 上午10:48:37 
*
 */
@ValidationBean
@Data
public class Authorise extends DataEntity<Authorise> {
   
	public static final int AUTHORISE_TYPE=1;//授权
	
    public static  final int AUTHORISE_TYPE_ASSIGN=0;//指派
   
   
	@NotEmpty(message="账簿不能为空！")
	private String accountingBookId;
	
	@NotEmpty(message="指派/授权会计不能为空！")
	private String accountingId;
	
	@NotEmpty(message="授权类型不能为空！")
	private Integer authoriseType; 
}
