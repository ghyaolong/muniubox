
package com.taoding.mapper.salary;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;

/**
 * 客户员工社保公积金表DAO接口
 * @author csl
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CpaSalaryCompanyWelfareDao extends CrudDao<CpaSalaryCompanyWelfare> {
	
	/**
	 * 根据账期获取客户员工社保公积金列表
	 * @param customerId
	 * @param period
	 * @return
	 */
	public List<CpaSalaryCompanyWelfare> findList(@Param("customerId") String customerId,@Param("period") String period);

	/**
	 * 分页查询客户员工社保公积金列表信息
	 * @param maps
	 * @return
	 */
	public List<CpaSalaryCompanyWelfare> findAllByPage(Map<String,Object> maps);
	
	/**
	 * 修改客户社保公积金信息
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	public int updateWelfare(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 初始化客户社保公积金信息
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	public void initCpaSalaryCompanyWelfare(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 根据员工id查找CpaSalaryCompanyWelfare中的员工id
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	public CpaSalaryCompanyWelfare findByEmployeeId(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 根据方案id获取客户社保公积金信息列表
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	public List<CpaSalaryCompanyWelfare> findByWelfareId(CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare);
	
	/**
	 * 获取企业公司缴纳的五险一金的总和
	 * @param companyWelfare
	 * @return
	 */
	public Double getCompanyTax(CpaSalaryCompanyWelfare companyWelfare);
	
	/**
	 * 获取单位缴纳员工的五险一金的各分项的和
	 * @param customerId
	 * @return
	 */
	public CpaSalaryCompanyWelfare getCompanySocialSecurity(CpaSalaryCompanyWelfare companyWelfare);
}