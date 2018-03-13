package com.taoding.mapper.salary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryInfo;

/**
 * 员工薪酬DAO接口
 * @author csl
 * @version 2017-12-13
 */
@Repository
@Mapper
public interface CpaSalaryInfoDao extends CrudDao<CpaSalaryInfo> {
	
	/**
	 * 查询薪酬列表+分页
	 * @param maps
	 * @return
	 */
	public List<CpaSalaryInfo> findAllByPage(Map<String, Object> maps);
	
	/**
	 * 根据薪资的id查找薪资对象信息
	 * @param id
	 * @return
	 */
	public CpaSalaryInfo findSalaryById(String id);
	
	/**
	 * 根据客户id查询薪酬信息初始化标记的最大值
	 * @param customerId
	 * @return
	 */
	public Integer findMaxInitFlag(String customerId);
	
	/**
	 * 初始化薪酬信息
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Integer initCpaSalaryInfo(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 获取企业计提工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getAccruedSalary(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 获取代扣五险一金
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getWithHoldAccount(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 获取代扣个税
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getIndividualTax(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 现金发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getCashSalary(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 银行发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
	public Double getBankSalary(CpaSalaryInfo cpaSalaryInfo);
	
	/**
	 * 获取企业的个人缴纳的社保公积金--各分项的总和
	 * @param customerId
	 * @return
	 */
	public CpaSalaryCompanyWelfare getIndividualSocialSecurity(CpaSalaryInfo cpaSalaryInfo);
	
}