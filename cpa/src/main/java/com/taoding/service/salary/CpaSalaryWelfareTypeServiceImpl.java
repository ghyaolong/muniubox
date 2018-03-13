
package com.taoding.service.salary;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryWelfareType;
import com.taoding.mapper.salary.CpaSalaryWelfareTypeDao;

/**
 * 社保公积金缴纳项目表Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
public class CpaSalaryWelfareTypeServiceImpl extends DefaultCurdServiceImpl<CpaSalaryWelfareTypeDao, CpaSalaryWelfareType>
	implements CpaSalaryWelfareTypeService{

	/**
	 * 查询所有的社保公积金缴纳项目名称
	 * @param
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareType> findAllNameList() {
		return dao.findAllName();
	}

	
}