package com.taoding.mapper.salary;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryBankInfo;


@Repository
@Mapper
public interface CpaSalaryBankInfoDao extends CrudDao<CpaSalaryBankInfo> {

	/**
	 * 插入数据
	 * @author fc 
	 * @version 2017年12月21日 下午5:41:12 
	 * @param cpaSalaryBankInfo
	 * @return
	 */
	public int insertSetting(CpaSalaryBankInfo cpaSalaryBankInfo);
	
	/**
	 * 更新数据
	 * @author fc 
	 * @version 2017年12月21日 下午5:41:14 
	 * @param cpaSalaryBankInfo
	 * @return
	 */
	public int updateSetting(CpaSalaryBankInfo cpaSalaryBankInfo);
	
	/**
	 * 获取当前客户薪酬设置
	 * @author fc 
	 * @version 2017年12月22日 上午9:27:19 
	 * @return
	 */
	public CpaSalaryBankInfo getSalarysetting(@Param("customerId") String customerId);
	
}
