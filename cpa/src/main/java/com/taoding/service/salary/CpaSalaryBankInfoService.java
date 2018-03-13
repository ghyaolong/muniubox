package com.taoding.service.salary;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryBankInfo;
import com.taoding.mapper.salary.CpaSalaryBankInfoDao;

public interface CpaSalaryBankInfoService extends CrudService<CpaSalaryBankInfoDao, CpaSalaryBankInfo> {

	/**
	 * 保存薪酬设置
	 * @author fc 
	 * @version 2017年12月21日 下午5:25:52 
	 * @param cpaSalaryBankInfo
	 * @return
	 */
	public boolean saveSalarySetting(CpaSalaryBankInfo cpaSalaryBankInfo);
	
	/**
	 * 获取当前客户薪酬设置
	 * @author fc 
	 * @version 2017年12月22日 上午9:27:19 
	 * @return
	 */
	public CpaSalaryBankInfo getSalarysetting(String customerId);
}
