package com.taoding.service.salary;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryInfo;
import com.taoding.mapper.salary.CpaSalaryInfoDao;

/**
 * 员工薪酬Service
 * @author csl
 * @version 2017-12-13
 */
public interface CpaSalaryInfoService extends CrudService<CpaSalaryInfoDao, CpaSalaryInfo> {
	
	/**
	 * 查询薪酬列表+分页
	 * @param maps
	 * @return
	 */
	public PageInfo<CpaSalaryInfo>  findAllByPage(Map<String,Object> maps);
	
	/**
	 * 根据薪资id编辑员工信息
	 * @param maps
	 * @return
	 */
	public Object updateEmployeeById(Map<String,Object> maps);
	
	/**
	 * 根据薪资id删除员工
	 * @param id
	 * @return
	 */
	public Object deleteEmployeeById(String id);
	
	/**
	 * 快速调整薪资列表信息
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateSalaryInfo(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 修改薪酬相关的信息后必须刷新薪酬列表
	 * @param customerId
	 * @return
	 */
	public Object refreshSalaryInfo(String customerId);
	
	/**
	 * 批量设置工资(传递薪资对象包含有基本工资，补助和customerId)
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object batchUpdateSalary(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 薪资批量设置社保公积金基数
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	public Object batchUpdateBasic(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 批量调整本期个税
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateAdjustTax(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 批量删除员工和薪酬
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object batchDeleteSalary(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 调整个税起征点
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Object updateIndividualTaxLevy(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 根据客户id删除企业的薪酬信息（初始化薪酬信息）
	 * @param customerId
	 * @return
	 */
	public Object deleteSalaryInfoByCustomerId(String customerId);
	
	/**
	 * 获取计提工资
	 * @param customerId
	 * @return
	 */
	public Double getAccruedSalary(String bookId,String customerId);
	
	/**
	 * 获取代扣五险一金
	 * @param customerId
	 * @return
	 */
	public Double getWithHoldAccount(String bookId,String customerId);
	
	/**
	 * 获取代扣个税
	 * @param customerId
	 * @return
	 */
	public Double getIndividualTax(String bookId,String customerId);
	
	/**
	 * 现金发放工资
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public Double getCashSalary(String bookId,String customerId);
	
	/**
	 * 银行发放工资
	 * @param bookId
	 * @param customerId
	 * @return
	 */
	public Double getBankSalary(String bookId,String customerId);
	
	/**
	 * 获取企业的个人缴纳的社保公积金的分项信息
	 * @param customerId
	 * @return
	 */
	public Map<String,BigDecimal> getIndividualSocialSecurity(String bookId,String customerId);
}