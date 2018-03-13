
package com.taoding.domain.salary;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import lombok.Data;
import lombok.ToString;

/**
 * 社保公积金模板Entity
 * @author csl
 * @version 2017-11-24
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryWelfareTemplate extends DataEntity<CpaSalaryWelfareTemplate> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=64, message="城市id长度必须介于 1 和 64 之间")
	@NotNull(message="城市id不能为空!")
	private String cityId;		// 城市id
	
	@Length(min=1, max=200, message="城市名称长度必须介于 1 和 200 之间")
	@NotNull(message="城市名称不能为空!")
	private String cityName;		// 城市名称
	
	@Length(min=1, max=64, message="项目类型id长度必须介于 1 和 64 之间")
	@NotNull(message="项目类型id不能为空!")
	private String welfareItemTypeId;		// 项目类型id
	
	@NotNull(message="公司缴纳比例不能为空!")
	private String companyRate;		// 公司缴纳比例
	
	
	private String companyAddition;		// 公司附加金额
	
	@NotNull(message="公司缴纳的固定金额不能为空!")
	private String companyFix;		// 公司缴纳的固定金额
	
	@Length(min=0, max=1, message="公司取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）长度必须介于 0 和 1 之间")
	private String companyRoundRuleId;		// 公司取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）
	
	private String individualRate;		// 个人比例
	
	private String individualAddition;		// 个人附加金额
	
	private String individualFix;		// 个人固定金额
	
	private String individualRoundRuleId;		// 个人取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）
	
	private String no;		// 类型编号的目的：为了能免快速找到某一个社保项            1：养老保险；            2：医疗保险；            3：失业保险；            4：生育保险；            5：工商保险；            6：公积金
	
	private String isDefault; //是否是默认模板     1：表示是默认模板
	
	//默认模板
	public static final String IS_DEFAULT="1";
}