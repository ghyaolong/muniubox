
package com.taoding.service.assisting;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaCustomerAssistingInfo;
import com.taoding.mapper.assisting.CpaCustomerAssistingInfoDao;


/**
 * 辅助核算Service
 * @author csl
 * @version 2017-11-16
 */
public interface CpaCustomerAssistingInfoService extends CrudService<CpaCustomerAssistingInfoDao, CpaCustomerAssistingInfo> {

	/**
	 * 根据id查询列表
	 * @param queryMap
	 * @return
	 * add csl
	 * 2017-11-18 11:53:37
	 */
	public Object findListById(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型的详细条目
	 * @param cpaCustomerAssistingInfo
	 * add csl
	 * 2017-11-18 15:54:11
	 */
	public Object insertCpaCustomerAssistingInfo(Map<String, Object> queryMap);
	
	/**
	 * 根据名字查找
	 * @param name
	 * @param accountId
	 * @return
	 * add csl
	 * 2017-11-18 16:51:47
	 */
//	public CpaCustomerAssistingInfo findByName(String name);
	
	/**
	 * 编辑辅助核算详情条目中的信息
	 * @param cpaCustomerAssistingInfo
	 * @return
	 * add csl
	 * 2017-11-18 16:51:47
	 */
	public Object updateCpaCustomerAssistingInfo(CpaCustomerAssistingInfo cpaCustomerAssistingInfo);
	

	
}