
package com.taoding.domain.salary;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

/**
 * 客户社保公积金配置Entity
 * @author csl
 * @version 2017-11-24
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryWelfareSetting extends DataEntity<CpaSalaryWelfareSetting> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=64, message="客户id长度必须介于 1 和 64 之间")
//	@NotNull(message="客户id不能为空！")
	private String customerId;		// 客户id
	
	@Length(min=1, max=200, message="方案名称id长度必须介于 1 和 64之间")
//	@NotNull(message="方案名称id不能为空！")
	private String projectId;		// 方案名称id
	
	@Length(min=1, max=64, message="缴纳项目类型id长度必须介于 1 和 64 之间")
//	@NotNull(message="缴纳项目类型id不能为空！")
	private String welfareItemTypeId;		// 缴纳项目类型id
	
	private BigDecimal companyRate;		// 公司缴纳比例
	
	private BigDecimal companyFix;		// 公司缴纳的固定金额
	
	@Length(min=0, max=1, message="公司取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）长度必须介于 0 和 1 之间")
	private String companyRoundRuleId;		// 公司取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）
	
	private BigDecimal individualRate;		// 个人比例
	private BigDecimal individualFix;		// 个人固定金额
	
	@Length(min=0, max=1, message="个人取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）长度必须介于 0 和 1 之间")
	private String individualRoundRuleId;		// 个人取整规则id（1：见角进元， 2：见分进角， 3：四舍五入到元，4：四舍五入到分， 5：四舍五入到角）
	
	@Length(min=0, max=4, message="类型编号的目的：为了能免快速找到某一个社保项            1：养老保险；2：医疗保险；3：失业保险；4：生育保险；5：工商保险；6 公积金长度必须介于 0 和 4 之间")
	private String no = "1";		// 类型编号的目的：为了能免快速找到某一个社保项            1：养老保险；2：医疗保险；3：失业保险；4：生育保险；5：工商保险；6 公积金
	
	private Integer initFlag;   //初始化标记
	
	private List<CpaSalaryWelfareSetting> lists;
	
	private CpaSalaryWelfareProject cpaSalaryWelfareProject;

	
	//该数据只是用来存储数据
	private String projectName;
	private String name;
	private String companyRoundRule;
	private String individualRoundRule;
}