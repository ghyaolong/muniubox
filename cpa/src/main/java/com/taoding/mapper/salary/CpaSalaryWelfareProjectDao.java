
package com.taoding.mapper.salary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryWelfareProject;

/**
 * 客户社保公积金方案DAO接口
 * @author csl
 * @version 2017-11-28
 */
@Repository
@Mapper
public interface CpaSalaryWelfareProjectDao extends CrudDao<CpaSalaryWelfareProject> {
	
	/**
	 * 查询所有的社保公积金方案的名称
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareProject> findAllList(String customerId);
	
	/**
	 * 删除所有的方案名称
	 * @param customerId
	 * @return
	 */
	public Object deleteProjectByCustomerId(String customerId);
	
	/**
	 * 初始化社保方案名称
	 * @param project
	 */
	public void initCpaSalaryWelfareProject(CpaSalaryWelfareProject project);
	
	/**
	 * 
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareProject> findProjectAndItemByCustomerId(String customerId);
	
	/**
	 * 根据方案名称获取对象
	 * @param project
	 * @return
	 */
	public CpaSalaryWelfareProject getProjectByProjectName(CpaSalaryWelfareProject project);
}