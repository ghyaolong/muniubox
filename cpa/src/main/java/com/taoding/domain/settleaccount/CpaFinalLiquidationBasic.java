package com.taoding.domain.settleaccount;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 企业期末结转基础配置
 * 
 * @author czy 2017年12月19日 上午9:20:39
 */
@Data
@ToString
@ValidationBean
public class CpaFinalLiquidationBasic extends DataEntity<CpaFinalLiquidationBasic> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2439422490277627436L;

	// 1、小规模 2、一般
	private String taxpayerProperty;
	// 期末结转分项键
	private String subKey;
	// 期末结转分项名称
	private String subName;
	// 科目ID
	private String subjectId;
	// 科目编码
	private String subjectNo;
	// 科目名称
	private String subjectName;
	
	private boolean direction;
	
	private Integer type ; 

}
