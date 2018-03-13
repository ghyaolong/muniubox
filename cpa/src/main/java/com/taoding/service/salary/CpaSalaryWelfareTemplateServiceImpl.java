
package com.taoding.service.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.salary.CpaSalaryWelfareTemplate;
import com.taoding.mapper.salary.CpaSalaryWelfareTemplateDao;

/**
 * 社保公积金模板Service
 * @author csl
 * @version 2017-11-24
 */
@Service
@Transactional
public class CpaSalaryWelfareTemplateServiceImpl extends DefaultCurdServiceImpl<CpaSalaryWelfareTemplateDao, CpaSalaryWelfareTemplate> 
	implements CpaSalaryWelfareTemplateService{

	@Autowired
	private CpaSalaryWelfareTemplateDao tempDao;
	
	/**
	 * 获取所有的模板数据
	 * @param isDefault
	 * @return 
	 */
	@Override
	public List<CpaSalaryWelfareTemplate> findListByDefault(String isDefault) {
		return dao.findListByDefault(isDefault);
	}

	/**
	 * 根据城市名称获取所有的社保公积金方案的模板
	 * @param  cityName
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareTemplate> findListByCityName(String cityName) {
		List<CpaSalaryWelfareTemplate> cityNameList = this.findAllCityName();
		for (CpaSalaryWelfareTemplate name : cityNameList) {
			System.out.println(name);
			if (name.getCityName().equals(cityName)) {
				return dao.findListByCityName(cityName);
			}
		}
		throw new LogicException("你所查询的城市名称不存在社保公积金模板方案！");
	}

	/**
	 * 查询所有的城市名称
	 * @param
	 * @return
	 */
	@Override
	public List<CpaSalaryWelfareTemplate> findAllCityName() {
		return tempDao.findAllCityName();
	}

	
}