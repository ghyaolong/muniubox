
package com.taoding.service.salary;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;
import com.taoding.mapper.salary.CpaSalaryCompanyWelfareDao;

/**
 * 客户员工社保公积金表Service
 * @author csl
 * @version 2017-11-24
 */
public interface CpaSalaryCompanyWelfareService extends CrudService<CpaSalaryCompanyWelfareDao, CpaSalaryCompanyWelfare> {

	
	/**
	 * 下载客户社保公积金列表数据
	 * @param maps
	 * @return
	 */
	public PageInfo<CpaSalaryCompanyWelfare> findWelfare(Map<String,Object> maps);
	
	/**
	 * 快速调整客户社保公积金详情
	 * @param companyWelfare
	 * @return
	 * @throws ParseException 
	 */
	public Object saveWelfare(CpaSalaryCompanyWelfare companyWelfare);
	
	/**
	 * 执行计算后的医疗的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getYiliaoData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 执行计算后的养老的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getYanglaoData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 获取计算后的失业的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getShiyeData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 获取计算后的生育的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getShengyuData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 获取计算后的工伤的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getGongshangData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 获取计算后的公积金的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getGongjijinData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 获取计算后的大病的费用
	 * @param cpaSalaryCompanyWelfare
	 * @param cpaSalaryWelfareSetting
	 */
	public void getDabingData(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare,CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 初始化客户社保公积金信息
	 * @param cpaSalaryCompanyWelfare
	 */
	public void initCpaSalaryCompanyWelfare(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 获取企业公司缴纳的五险一金的总和
	 * @param customerId
	 * @return
	 */
	public Double getCompanyTax(String bookId,String customerId);
	
	/**
	 * 获取单位缴纳的五险一金的各分项和
	 * @param customerId
	 * @return
	 */
	public Map<String,BigDecimal> getCompanySocialSecurity(String bookId,String customerId);
}