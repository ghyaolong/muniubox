
package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryRoundRule;
import com.taoding.mapper.salary.CpaSalaryRoundRuleDao;

/**
 * 取整规则Service
 * @author csl
 * @version 2017-11-24
 */
public interface CpaSalaryRoundRuleService extends CrudService<CpaSalaryRoundRuleDao, CpaSalaryRoundRule> {

	/**
	 * 查询所有的取整规则
	 * @param
	 * @return
	 */
	public List<CpaSalaryRoundRule> findRuleList();
	
}