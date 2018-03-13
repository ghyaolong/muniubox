
package com.taoding.service.salary;

import java.util.List;
import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryWelfareProject;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;
import com.taoding.mapper.salary.CpaSalaryWelfareSettingDao;

/**
 * 客户社保公积金配置Service
 * @author csl
 * @version 2017-11-24
 */
public interface CpaSalaryWelfareSettingService extends CrudService<CpaSalaryWelfareSettingDao, CpaSalaryWelfareSetting> {

//	/**
//	 * 根据账薄id查找所有的社保/公积金项目
//	 * @param customerId
//	 * @return
//	 */
//	public Map<String, List<CpaSalaryWelfareSetting>> findNameByCutomerId(String customerId);
	
	/**
	 * 根据id删除企业的社保公积金缴纳的项目
	 * @param cpaSalaryWelfareSetting
	 * @return
	 */
	public Object deleteById(String id);
	
	/**
	 * 獲取所有的社保公积金方案名称
	 * @param 
	 * @return
	 */
	public Object findAllName();
	
	/**
	 * 根据方案名称删除方案
	 * @param projectId
	 * @param customerId
	 * @return
	 */
	public Object deleteByName(String projectId,String customerId);
	
	/**
	 * 修改或者新增企业社保公积金方案
	 * @param maps
	 * @return
	 */
	public Object saveProject(CpaSalaryWelfareSetting cpaSalaryWelfareSetting);
	
	/**
	 * 根据方案名称获取企业的方案列表
	 * @param CpaSalaryWelfareSetting
	 * @return
	 */
	public List<CpaSalaryWelfareSetting> findCustomerIdByWelfareId(CpaSalaryWelfareSetting CpaSalaryWelfareSetting);
	
	/**
	 * 根据客户id删除企业的社保公积金方案(初始化社保公积金方案和客户社保公积金信息列表)
	 * @param customerId
	 * @return
	 */
	public Object deleteProjectByCustomerId(String customerId);
	
	/**
	 * 初始化社保公积金模板数据
	 * @param customerId
	 * @return
	 */
	public Object  initSalaryProjectData(String customerId);
	
	/**
	 * 社保公积金方案保存成功后，必须刷新企业客户社保公积金信息表
	 * @param customerId
	 * @return
	 */
	public Object refreshWelfare(String customerId);
	
	/**
	 * 根据客户id获取所有的企业社保公积金方案
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareProject> findProjectAndItemByCutomerId(String customerId);
}