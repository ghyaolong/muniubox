
package com.taoding.service.salary;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryRoundRule;
import com.taoding.mapper.salary.CpaSalaryRoundRuleDao;

/**
 * 取整规则Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
public class CpaSalaryRoundRuleServiceImpl extends DefaultCurdServiceImpl<CpaSalaryRoundRuleDao, CpaSalaryRoundRule> 
	implements CpaSalaryRoundRuleService{

	/**
	 * 查询所有的取整规则
	 * @param
	 * @return
	 */
	@Override
	public List<CpaSalaryRoundRule> findRuleList() {
		return dao.findAllList();
	}
	
	
}