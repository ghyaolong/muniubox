package com.taoding.domain.settleaccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 资产类科目余额 VO
 * @author admin
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetClassBalanceVO {
	
		// 科目Key
		private String subjectKey;

		// 科目名称
		private String subjectName;

		//状态
		private boolean state ;
		
		private String message ;
		
}
