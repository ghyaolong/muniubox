package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.mapper.salary.CpaSalaryEmployeeDao;

/**
 * 社保公积金员工表Service
 * @author csl
 * @version 2017-12-13
 */
public interface CpaSalaryEmployeeService extends CrudService<CpaSalaryEmployeeDao, CpaSalaryEmployee> {

	/**
	 * 薪酬中新增员工
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public Object addEmployee(CpaSalaryEmployee cpaSalaryEmployee);
	
	/**
	 * 查询企业所有的员工
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public List<CpaSalaryEmployee> findSalaryEmployeeList(CpaSalaryEmployee cpaSalaryEmployee);
	
	/**
	 * 导入员工数据
	 * @param list
	 * @param customerId
	 * @return
	 */
	public Object importEmployeeData(List<CpaSalaryEmployee> list,String customerId);

}