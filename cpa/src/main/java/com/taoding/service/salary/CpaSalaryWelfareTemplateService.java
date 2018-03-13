
package com.taoding.service.salary;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.salary.CpaSalaryWelfareTemplate;
import com.taoding.mapper.salary.CpaSalaryWelfareTemplateDao;

/**
 * 社保公积金模板Service
 * @author csl
 * @version 2017-11-24
 */
public interface CpaSalaryWelfareTemplateService extends CrudService<CpaSalaryWelfareTemplateDao, CpaSalaryWelfareTemplate> {

	/**
	 * 获取所有的模板数据
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findListByDefault(String isDefault);
	
	/**
	 * 根据城市名称获取所有的城市模板方案名称
	 * @param cityName
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findListByCityName(String cityName);
	
	/**
	 * 查询所有的城市名称
	 * @param
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findAllCityName();
}