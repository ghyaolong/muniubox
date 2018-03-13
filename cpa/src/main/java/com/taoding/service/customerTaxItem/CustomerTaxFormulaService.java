package com.taoding.service.customerTaxItem;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.mapper.customerTaxItem.CustomerTaxFormulaDao;

/**
 * 税项税项公式Service
 * 
 * @author mhb
 * @version 2017-11-22
 */
public interface CustomerTaxFormulaService extends
		CrudService<CustomerTaxFormulaDao, CustomerTaxFormula> {

	/**
	 * 新增客户税项公式
	 * 
	 * @param customerTaxItem
	 *            实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object saveTaxFormula(CustomerTaxFormula customerTaxFormula);
	/**
	 * 查询客户税项公式
	 * @param queryMap
	 * @return
	 * @author mhb
	 * @Date 2017年11月28日
	 */
	Object findList(Map<String, Object> queryMap);

}
