package com.taoding.service.customerTaxItem;

import com.taoding.common.service.CrudService;
import com.taoding.domain.customerTaxItem.TaxTemplate;
import com.taoding.mapper.customerTaxItem.TaxTemplateDao;

/**
 * 税项模板Service
 * 
 * @author mhb
 * @version 2017-11-22
 */
public interface TaxTemplateService extends
		CrudService<TaxTemplateDao, TaxTemplate> {
	/**
	 * 新增税项
	 * 
	 * @param taxTemplate
	 *            实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object saveTemplate(TaxTemplate taxTemplate);

	/**
	 * 修改税项
	 * 
	 * @param taxTemplate
	 *            实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object editTemplate(TaxTemplate taxTemplate);

	/**
	 * 删除税项
	 * 
	 * @param id
	 *            税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object deleteTemplate(String id);
	/**
	 * 启用 禁用税项
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	Object enableTemplate(String id);

}
