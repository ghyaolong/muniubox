
package com.taoding.domain.salary;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.assisting.CpaAssistingEmployee;

import lombok.Data;
import lombok.ToString;

/**
 * 客户员工社保公积金表Entity
 * @author csl
 * @version 2017-11-24
 */
@Data
@ToString
@ValidationBean
public class CpaSalaryCompanyWelfare extends DataEntity<CpaSalaryCompanyWelfare> {
	
	private static final long serialVersionUID = 1L;
	
	@Length(min=1, max=64, message="员工编号长度必须介于 1 和 64 之间")
	private String employeeId;		// 员工编号
	
//	@NotNull(message="账期不能为空")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date period;		// 账期
	
	@Length(min=1, max=64, message="缴费方案id长度必须介于 1 和 64 之间")
	private String welfareSettingId;		// 缴费方案id
	
	@Length(min=1, max=64, message="客户id长度必须介于 1 和 64 之间")
	private String customerId;		// 客户id
	
	private BigDecimal yanglaoBasic= new BigDecimal(0);		// 养老保险基数
	private BigDecimal yanglaoIndividual= new BigDecimal(0);		// 养老保险个人
	private BigDecimal yanglaoCompany= new BigDecimal(0);		// 养老保险单位
	private BigDecimal yiliaoBasic= new BigDecimal(0);		// 医疗保险基数
	private BigDecimal yiliaoIndividual= new BigDecimal(0);		// 医疗保险个人
	private BigDecimal yiliaoCompany= new BigDecimal(0);		// 医疗保险单位
	private BigDecimal shiyeBasic= new BigDecimal(0);		// 失业保险基数
	private BigDecimal shiyeIndividual= new BigDecimal(0);		// 失业保险个人
	private BigDecimal shiyeCompany= new BigDecimal(0);		// 失业保险单位
	private BigDecimal shengyuBasic= new BigDecimal(0);		// 生育保险基数
	private BigDecimal shengyuCompany= new BigDecimal(0);		// 生育保险单位
	private BigDecimal gongshangBasic= new BigDecimal(0);		// 工伤保险基数
	private BigDecimal gongshangCompany= new BigDecimal(0);		// 工伤保险单位
	private BigDecimal gongjijinBasic= new BigDecimal(0);		// 公积金基数
	private BigDecimal gongjijinIndividual= new BigDecimal(0);		// 公积金个人
	private BigDecimal gongjijinCompany= new BigDecimal(0);		// 公积金单位
	private BigDecimal countIndividual= new BigDecimal(0);		// 合计个人
	private BigDecimal countCompany= new BigDecimal(0);		// 合计单位
	private BigDecimal dabingBasic= new BigDecimal(0);     //大病保险基数
	private BigDecimal dabingCompany= new BigDecimal(0);    //大病保险单位
	
	private Integer initFlag;   //初始化标记
	
	private CpaAssistingEmployee assistingEmployee;
	
	//以下参数只是用于存储数据，并没有存在于数据库中
	private String departmentId;
	private String employeeName;
	private String projectName;
	//用于传递修改的客户社保公积金信息
	private List<CpaSalaryCompanyWelfare> list;

//	public String getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(String employeeId) {
//		this.employeeId = employeeId;
//	}
//
//	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
//	public Date getPeriod() {
//		return period;
//	}
//
//	public void setPeriod(Date period) {
//		this.period = period;
//	}
//
//	public String getWelfareSettingId() {
//		return welfareSettingId;
//	}
//
//	public void setWelfareSettingId(String welfareSettingId) {
//		this.welfareSettingId = welfareSettingId;
//	}
//
//	public String getAccountId() {
//		return accountId;
//	}
//
//	public void setAccountId(String accountId) {
//		this.accountId = accountId;
//	}
//
//	public BigDecimal getYanglaoBasic() {
//		return yanglaoBasic;
//	}
//
//	public void setYanglaoBasic(BigDecimal yanglaoBasic) {
//		this.yanglaoBasic = yanglaoBasic;
//	}
//
//	public void setYanglaoIndividual(BigDecimal yanglaoIndividual) {
//		this.yanglaoIndividual = yanglaoIndividual;
//	}
//
//
//	public void setYanglaoCompany(BigDecimal yanglaoCompany) {
//		this.yanglaoCompany = yanglaoCompany;
//	}
//
//
//	public void setYiliaoBasic(BigDecimal yiliaoBasic) {
//		this.yiliaoBasic = yiliaoBasic;
//	}
//
//	public BigDecimal getYiliaoIndividual() {
//		return yiliaoIndividual;
//	}
//
//	public void setYiliaoIndividual(BigDecimal yiliaoIndividual) {
//		this.yiliaoIndividual = yiliaoIndividual;
//	}
//
//	public BigDecimal getYiliaoCompany() {
//		return yiliaoCompany;
//	}
//
//	public BigDecimal getShiyeBasic() {
//		return shiyeBasic;
//	}
//
//	public void setShiyeBasic(BigDecimal shiyeBasic) {
//		this.shiyeBasic = shiyeBasic;
//	}
//
//
//	public void setShiyeIndividual(BigDecimal shiyeIndividual) {
//		this.shiyeIndividual = shiyeIndividual;
//	}
//
//
//	public void setShiyeCompany(BigDecimal shiyeCompany) {
//		this.shiyeCompany = shiyeCompany;
//	}
//
//	public BigDecimal getShengyuBasic() {
//		return shengyuBasic;
//	}
//
//	public void setShengyuBasic(BigDecimal shengyuBasic) {
//		this.shengyuBasic = shengyuBasic;
//	}
//
//	public void setShengyuCompany(BigDecimal shengyuCompany) {
//		this.shengyuCompany = shengyuCompany;
//	}
//
//	public BigDecimal getGongshangBasic() {
//		return gongshangBasic;
//	}
//
//	public void setGongshangBasic(BigDecimal gongshangBasic) {
//		this.gongshangBasic = gongshangBasic;
//	}
//
//	public void setGongshangCompany(BigDecimal gongshangCompany) {
//		this.gongshangCompany = gongshangCompany;
//	}
//
//	public BigDecimal getGongjijinBasic() {
//		return gongjijinBasic;
//	}
//
//	public void setGongjijinBasic(BigDecimal gongjijinBasic) {
//		this.gongjijinBasic = gongjijinBasic;
//	}
//
//
//	public void setGongjijinIndividual(BigDecimal gongjijinIndividual) {
//		this.gongjijinIndividual = gongjijinIndividual;
//	}
//
//
//	public void setGongjijinCompany(BigDecimal gongjijinCompany) {
//		this.gongjijinCompany = gongjijinCompany;
//	}
//
//	public BigDecimal getCountIndividual() {
//		return countIndividual;
//	}
//
//	public void setCountIndividual(BigDecimal countIndividual) {
//		this.countIndividual = countIndividual;
//	}
//
//	public BigDecimal getCountCompany() {
//		return countCompany;
//	}
//
//	public void setCountCompany(BigDecimal countCompany) {
//		this.countCompany = countCompany;
//	}
//	
//	public BigDecimal getDabingBasic() {
//		return dabingBasic;
//	}
//
//	public void setDabingBasic(BigDecimal dabingBasic) {
//		this.dabingBasic = dabingBasic;
//	}	
//
//	public void setDabingCompany(BigDecimal dabingCompany) {
//		this.dabingCompany = dabingCompany;
//	}
//	
//	@ExcelField(title="姓名",align=2, sort=1,value="assistingEmployee.name")
//	public CpaAssistingEmployee getAssistingEmployeeName() {
//		return assistingEmployee;
//	}
//	
//	@ExcelField(title="医疗保险个人缴纳",align=2, sort=2)
//	public BigDecimal getYiliaoBasic() {
//		return yiliaoBasic;
//	}
//	
//	@ExcelField(title="医疗保险公司缴纳",align=2, sort=3)
//	public void setYiliaoCompany(BigDecimal yiliaoCompany) {
//		this.yiliaoCompany = yiliaoCompany;
//	}
//	
//	@ExcelField(title="养老保险个人缴纳",align=2, sort=4)
//	public BigDecimal getYanglaoIndividual() {
//		return yanglaoIndividual;
//	}
//	
//	@ExcelField(title="养老保险公司缴纳",align=2, sort=5)
//	public BigDecimal getYanglaoCompany() {
//		return yanglaoCompany;
//	}
//	
//	@ExcelField(title="失业保险个人缴纳",align=2, sort=6)
//	public BigDecimal getShiyeIndividual() {
//		return shiyeIndividual;
//	}
//	
//	@ExcelField(title="失业保险公司缴纳",align=2, sort=7)
//	public BigDecimal getShiyeCompany() {
//		return shiyeCompany;
//	}
//	
//	@ExcelField(title="工伤保险公司缴纳",align=2, sort=8)
//	public BigDecimal getGongshangCompany() {
//		return gongshangCompany;
//	}
//	
//	@ExcelField(title="生育保险公司缴纳",align=2, sort=9)
//	public BigDecimal getShengyuCompany() {
//		return shengyuCompany;
//	}
//
//	@ExcelField(title="大病保险公司缴纳",align=2, sort=10)
//	public BigDecimal getDabingCompany() {
//		return dabingCompany;
//	}
//
//	@ExcelField(title="公积金个人缴纳",align=2, sort=11)
//	public BigDecimal getGongjijinIndividual() {
//		return gongjijinIndividual;
//	}
//	
//	@ExcelField(title="公积金公司缴纳",align=2, sort=12)
//	public BigDecimal getGongjijinCompany() {
//		return gongjijinCompany;
//	}
//
//	public void setAssistingEmployee(CpaAssistingEmployee assistingEmployee) {
//		this.assistingEmployee = assistingEmployee;
//	}
}