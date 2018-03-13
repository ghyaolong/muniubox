package com.taoding.mapper.customerTaxItem;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;

/**
 * 税项模板Dao
 * 
 * @author mhb
 * @version 2017-11-22
 */
@Repository
@Mapper
public interface CustomerTaxFormulaDao extends CrudDao<CustomerTaxFormula> {
	/**
	 * 查询客户税项公式
	 * @param queryMap
	 * @return
	 * @author mhb
	 * @Date 2017年11月28日
	 */
	List<CustomerTaxFormula> findList(Map<String, Object> queryMap);

}

