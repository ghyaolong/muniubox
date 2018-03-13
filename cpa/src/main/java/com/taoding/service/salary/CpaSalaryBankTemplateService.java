package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryBankTemplate;
import com.taoding.mapper.salary.CpaSalaryBankTemplateDao;

public interface CpaSalaryBankTemplateService extends CrudService<CpaSalaryBankTemplateDao, CpaSalaryBankTemplate> {

	
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
	public CpaSalaryBankTemplate getBankTemplateById(String id);
	
	/**
	 * 根据银行模板code获取银行模板详情
	 * @author fc 
	 * @version 2017年12月21日 下午4:43:08 
	 * @return
	 */
	public CpaSalaryBankTemplate getBankTemplateByCode(String code);
}
