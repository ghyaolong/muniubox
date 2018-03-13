package com.taoding.domain.salary;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;

import lombok.Data;
import lombok.ToString;

/**
 * 社保公积金员工表Entity
 * @author csl
 * @version 2017-12-13
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryEmployee extends DataEntity<CpaSalaryEmployee> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=0, max=64, message="employee_no长度必须介于 0 和 64 之间")
	private String employeeNo;		// employee_no
	@ExcelField(title="姓名", align=2, sort=10)
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	private String employeeName;		// 姓名
	@ExcelField(title="性别", align=2, sort=20)
	@Length(min=1, max=1, message="男（true）或者女(false)长度必须介于 1 和 1 之间")
	private String gender;		// 男（true）或者女(false)
	
	@Length(min=1, max=64, message="所属部门长度必须介于 1 和 64 之间")
	private String departmentId;		// 所属部门
	
	@Length(min=1, max=64, message="职位长度必须介于 1 和 64 之间")
	private String positionId;		// 职位
	@ExcelField(title="手机号", align=2, sort=30)
	@Length(min=0, max=64, message="手机号长度必须介于 0 和 64 之间")
	private String phone;		// 手机号
	@ExcelField(title="邮箱", align=2, sort=40)
	@Email
	@Length(min=0, max=200, message="邮箱长度必须介于 0 和 200 之间")
	private String email;		// 邮箱
	@ExcelField(title="户口类型", align=2, sort=50)
	@Length(min=1, max=20, message="城镇户口/农村户口长度必须介于 1 和 20 之间")
	private String hukou;		// 城镇户口/农村户口
	@ExcelField(title="身份证号", align=2, sort=60)
	@Length(min=0, max=40, message="身份证号码长度必须介于 0 和 40 之间")
	private String identity;		// 身份证号码
	@ExcelField(title="人员状态", align=2, sort=70)
	@Length(min=0, max=16, message="正式，正式和离职长度必须介于 0 和 16 之间")
	private String status;		// 正式，正式和离职
	@ExcelField(title="入职时间", align=2, sort=80)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date onBoardData;		// 入职时间
	
	@Length(min=0, max=64, message="工 资卡银行长度必须介于 0 和 64 之间")
	private String bankId;		// 工 资卡银行
	
	@Length(min=1, max=4, message="是否将员工导出到薪酬(0：未导出，1 已导出)长度必须介于 1 和 4 之间")
	private String exportFlag;		// 是否将员工导出到薪酬(0：未导出，1 已导出)
	
	@Length(min=1, max=64, message="客户id长度必须介于 1 和 64 之间")
	private String customerId;		// 客户id
	@ExcelField(title="银行卡号", align=2, sort=100)
	@Length(min=0, max=64, message="银行卡号长度必须介于 0 和 64 之间")
	private String creditCard;		// 银行卡号
	
	private BigDecimal individualTaxLevy = new BigDecimal(0);   //个税起征点
	
	public static final String REGEX_CHECK_EMPLOYEENAME = "[a-zA-Z]{1,20}|[\u4e00-\u9fa5]{1,10}";
	public static final String REGEX_CHECK_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
	
	//以下字段只是用于存储数据
	@ExcelField(title="基本工资", align=2, sort=110)
	private String salary;
	@ExcelField(title="补助", align=2, sort=120)
	private String allowance;
	@ExcelField(title="工资卡银行", align=2, sort=90)
	private String bankName;
	
}