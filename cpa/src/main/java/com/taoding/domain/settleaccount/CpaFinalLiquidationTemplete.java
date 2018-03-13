package com.taoding.domain.settleaccount;

import java.io.Serializable;

import lombok.Data;

/**
 * 期末结转模板
 * 
 * @author czy 2017年12月22日 上午10:08:39
 */
@Data
public class CpaFinalLiquidationTemplete implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1690948191943844926L;

	// 纳税人性质 1小规模纳税人 2一般纳税人
	private String taxpayerProperty;
	
	// 期末结转分项键
	private String subKey;

	// 期末结转分项名称
	private String subName;

}
