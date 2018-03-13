package com.taoding.domain.report.assetliability;

import java.math.BigDecimal;
import java.util.List;

import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产负债类目录
 * @author Yang Ji Qiang
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Catalogue extends DataEntity<Catalogue>{
	
	private static final long serialVersionUID = 6094100174383254918L;


	//名称
	private String name;
	
	
	//汇总时用的名字
	private String accumulationName;
	
	//所有的大项目
	List<Type> typeList;
	
	//期末金额
	private BigDecimal endingBalanceOfFinance = BigDecimal.ZERO;
	
	//年初金额
	private BigDecimal beginningBalance = BigDecimal.ZERO;

}
