
package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryWelfareProject;
import com.taoding.mapper.salary.CpaSalaryWelfareProjectDao;

/**
 * 客户社保公积金方案Service
 * @author csl
 * @version 2017-11-28
 */
public interface CpaSalaryWelfareProjectService extends CrudService<CpaSalaryWelfareProjectDao, CpaSalaryWelfareProject> {

	/**
	 * 查询企业所有的社保公积金方案名称
	 * @param customerId
	 * @return
	 */
	public List<CpaSalaryWelfareProject> findAllList(String customerId);
}