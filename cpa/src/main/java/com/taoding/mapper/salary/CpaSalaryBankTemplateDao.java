package com.taoding.mapper.salary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryBankTemplate;


@Repository
@Mapper
public interface CpaSalaryBankTemplateDao extends CrudDao<CpaSalaryBankTemplate> {

	/**
	 * 薪酬设置中银行模板列表
	 * @author fc 
	 * @version 2017年12月21日 下午4:21:40 
	 * @return
	 */
	public List<CpaSalaryBankTemplate> salaryBankTemplateList();
	
	/**
	 * 根据银行模板id获取银行模板详情
	 * @author fc 
	 * @version 2017年12月21日 下午4:43:08 
	 * @return
	 */
	public CpaSalaryBankTemplate getBankTemplateById(@Param("id") String id);
	
	/**
	 * 根据银行模板code获取银行模板详情
	 * @author fc 
	 * @version 2017年12月21日 下午4:43:08 
	 * @return
	 */
	public CpaSalaryBankTemplate getBankTemplateByCode(@Param("code") String code);
}
