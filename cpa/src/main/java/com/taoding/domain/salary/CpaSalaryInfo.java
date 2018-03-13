package com.taoding.domain.salary;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;

import lombok.Data;
import lombok.ToString;

/**
 * 员工薪酬Entity
 * @author csl
 * @version 2017-12-13
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryInfo extends DataEntity<CpaSalaryInfo> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=64, message="员工id长度必须介于 1 和 64 之间")
	private String employeeId;		// 员工id
	
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date period;		// 账期
	
	@Length(min=1, max=64, message="客户id长度必须介于 1 和 64 之间")
	private String customerId;    //客户id
	@ExcelField(title="基本工资",align=2,sort=20,type=1)
	private BigDecimal salary = new BigDecimal(0) ;		// 基本工资
	@ExcelField(title="补助",align=2,sort=30,type=1)
	private BigDecimal allowance = new BigDecimal(0);		// 补助
	@ExcelField(title="扣款",align=2,sort=40,type=1)
	private BigDecimal withhold = new BigDecimal(0);		// 扣款
	@ExcelField(title="加班",align=2,sort=50,type=1)
	private BigDecimal workOvertime = new BigDecimal(0);		// 加班
	@ExcelField(title="应发工资",align=2,sort=60,type=1)
	private BigDecimal salaryCount = new BigDecimal(0);		// 应发工资
	@ExcelField(title="代扣合计",align=2,sort=110,type=1)
	private BigDecimal withholdCount = new BigDecimal(0);		// 代扣合计
	@ExcelField(title="应纳税所得额",align=2,sort=120,type=1)
	private BigDecimal taxBasic = new BigDecimal(0);		// 应纳税所得额
	@ExcelField(title="代扣个税",align=2,sort=130,type=1)
	private BigDecimal individualTax = new BigDecimal(0);		// 代扣个税
	@ExcelField(title="实发工资",align=2,sort=140,type=1)
	private BigDecimal finalPayAccount = new BigDecimal(0);		// 实发工资
	private BigDecimal taxFree = new BigDecimal(0);		// 免税所得
	private BigDecimal otherPretaxDeductions = new BigDecimal(0);		// 其他税前扣除
	private BigDecimal deductDonations = new BigDecimal(0);		// 准予扣除捐赠
	private BigDecimal taxDeductible = new BigDecimal(0);		// 允许扣除税费
	private BigDecimal lessTaxPaid = new BigDecimal(0);		// 减免税额
	
	private Integer initFlag;    //初始化标记
	
	private List<CpaSalaryInfo> lists;   //用于存放本类的信息
	//初始化的初始值
	public static final Integer INITGLAG_ZERO = 0;
	//以下属性用于存储
	@ExcelField(title="养老保险基数",align=2,sort=70,type=1)
	private BigDecimal yanglaoIndividual;
	@ExcelField(title="医疗保险基数",align=2,sort=80,type=1)
	private BigDecimal yiliaoIndividual;
	@ExcelField(title="失业保险基数",align=2,sort=90,type=1)
	private BigDecimal shiyeIndividual;
	@ExcelField(title="公积金保险基数",align=2,sort=100,type=1)
	private BigDecimal gongjijinIndividual;
	@ExcelField(title="姓名",align=2,sort=10,type=1)
	private String employeeName;
	private BigDecimal individualTaxLevy; 
	
	
	
}