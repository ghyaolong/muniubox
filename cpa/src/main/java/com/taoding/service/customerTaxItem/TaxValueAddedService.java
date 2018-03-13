package com.taoding.service.customerTaxItem;

import com.taoding.common.service.CrudService;
import com.taoding.domain.customerTaxItem.TaxValueAdded;
import com.taoding.mapper.customerTaxItem.TaxValueAddedDao;



/**
 * 客户增值税设置Service
 * 
 * @author mhb
 * @version 2017-11-27
 */
public interface TaxValueAddedService extends CrudService<TaxValueAddedDao, TaxValueAdded> {
	/**
	 * 客户增值税添加
	 * @param taxValueAdded  实体类  
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	Object insert(TaxValueAdded taxValueAdded);
	/**
	 * 查询增值税
	 * @param accountingId  账薄id 
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	Object getIncrementTax(String accountingId); 

}
