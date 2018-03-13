package com.taoding.service.customerTaxItem;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.mapper.customerTaxItem.CustomerTaxItemDao;



/**
 * 税项设置Service
 * 
 * @author mhb
 * @version 2017-11-22
 */
public interface CustomerTaxItemService extends CrudService<CustomerTaxItemDao, CustomerTaxItem> {
	/**
	 * 新增客户税项
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object saveTax(CustomerTaxItem customerTaxItem);
	/**
	 * 修改税项模板
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */

	Object editTax(CustomerTaxItem customerTaxItem);
	/**
	 * 删除税项模板
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object deleteTax(String id);
	/**
	 * 启用 禁用税项模板
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object enableTax(String id);
	/**
	 * 查询客户税项
	 * @param queryMap
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object findCustomerTaxList(Map<String, Object> queryMap);
	/**
	 * 插入客户税项模板 
	 * @param  accountingBook 账薄
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object saveCustomerTaxtemplate(AccountingBook accountingBook);
	/**
	 * 查询税项 
	 * @param  id 税项id
	 * @return
	 * @author mhb
	 * @Date 2017年12月08日
	 */
	Object selectTaxById(String id);

}
