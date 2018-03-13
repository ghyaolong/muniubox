package com.taoding.domain.report.assetliability;

import java.math.BigDecimal;
import java.util.Date;

import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 历史报表的汇总项
 * @author vincent
 *
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ReportHistory extends DataEntity<ReclassifySetting>{


	private static final long serialVersionUID = 3075581275597226293L;

	//账簿Id
	private String AccountingId;
	
	//账期
	private Date accountPeriod;
	
	//对应项目
	private Integer itemId;
	
	//行号
	private int lineNo;
	
	//类型
	private Type type;
	
	//父项目id
	private Integer parentId;
	
	//期末金额
	private BigDecimal endingBalanceOfFinance;
	
	//年初金额
	private BigDecimal beginningBalance;
}
