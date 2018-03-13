
package com.taoding.domain.salary;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 取整规则Entity
 * @author csl
 * @version 2017-11-24
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryRoundRule extends DataEntity<CpaSalaryRoundRule> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="取整规则名称长度必须介于 0 和 64 之间")
	@NotNull(message="取整规则名称不能为空！")
	private String ruleName;		// 取整规则名称
	
	public static final String CornerToYuan = "1";  //见角进元
	public static final String CentToCorner = "2";  //见分进角
	public static final String RoundAndRound = "3";  //四舍五入到分
	public static final String RoundToCorner = "4";  //四舍五入到角
	public static final String RoundToYuan = "5";  //四舍五入到元
}