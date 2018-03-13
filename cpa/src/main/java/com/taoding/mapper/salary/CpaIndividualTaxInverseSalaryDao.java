
package com.taoding.mapper.salary;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.salary.CpaIndividualTaxInverseSalary;

/**
 * 取整规则DAO接口
 * @author csl
 * @version 2017-11-24
 */
@Repository
@Mapper
public interface CpaIndividualTaxInverseSalaryDao extends CrudDao<CpaIndividualTaxInverseSalary> {
	
}