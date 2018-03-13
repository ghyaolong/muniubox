
package com.taoding.service.salary;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryWelfareProject;
import com.taoding.mapper.salary.CpaSalaryWelfareProjectDao;

/**
 * 客户社保公积金方案Service
 * @author csl
 * @version 2017-11-28
 */
@Service
@Transactional
public class CpaSalaryWelfareProjectServiceImpl extends DefaultCurdServiceImpl<CpaSalaryWelfareProjectDao, CpaSalaryWelfareProject>
	implements CpaSalaryWelfareProjectService{

	/**
	 * 查询企业所有的社保公积金方案名称
	 * @param customerId
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareProject> findAllList(String customerId) {
		return dao.findAllList(customerId);
	}

	
}