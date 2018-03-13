
package com.taoding.mapper.salary;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryWelfareTemplate;

/**
 * 社保公积金模板DAO接口
 * @author csl
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CpaSalaryWelfareTemplateDao extends CrudDao<CpaSalaryWelfareTemplate> {
	
	/**
	 * 根据名字获取所有的社保/公积金项目
	 * @param cityName
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findListByDefault(String isDefault);
	
	/**
	 * 根据城市名称获取所有的社保公积金方案模板
	 * @param cityName
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findListByCityName(@Param("cityName") String cityName);
	
	/**
	 * 查询所有的城市名称
	 * @param
	 * @return
	 */
	public List<CpaSalaryWelfareTemplate> findAllCityName();
}