package com.taoding.mapper.salary;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryEmployee;

/**
 * 社保公积金员工表DAO接口
 * @author csl
 * @version 2017-12-13
 */
@Repository
@Mapper
public interface CpaSalaryEmployeeDao extends CrudDao<CpaSalaryEmployee> {
	
	/**
	 * 根据名字查找对象
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public CpaSalaryEmployee findByName(CpaSalaryEmployee cpaSalaryEmployee);
	
	/**
	 * 查询编号的最大号码
	 * @param customerId
	 * @return
	 */
	public String findMaxNoByInfoNo(String customerId);
	
	/**
	 * 根据员工id编辑员工信息
	 * @param cpaSalaryEmployee
	 * @return
	 */
	public int updateEmployee(CpaSalaryEmployee cpaSalaryEmployee);
	
}