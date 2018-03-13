
package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryWelfareType;
import com.taoding.mapper.salary.CpaSalaryWelfareTypeDao;

/**
 * 社保公积金缴纳项目表Service
 * @author csl
 * @version 2017-11-24
 */
public interface CpaSalaryWelfareTypeService extends CrudService<CpaSalaryWelfareTypeDao, CpaSalaryWelfareType> {

	/**
	 * 查询所有的社保公积金缴纳项目的名称
	 * @param
	 * @return
	 */
	public List<CpaSalaryWelfareType> findAllNameList();
	
}