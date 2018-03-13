package com.taoding.mapper.salary;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaSalaryTaxrate;

/**
 * 税率DAO接口
 * @author csl
 * @version 2017-12-13
 */
@Repository
@Mapper
public interface CpaSalaryTaxrateDao extends CrudDao<CpaSalaryTaxrate> {
	
	/**
	 * 根据应纳税所得参数查找税率对象
	 * @param taxBasic
	 * @return
	 */
	public CpaSalaryTaxrate findByTaxBasic(@Param("taxMax") BigDecimal taxMax);
}