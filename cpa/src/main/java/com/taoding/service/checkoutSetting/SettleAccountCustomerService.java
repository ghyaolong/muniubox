package com.taoding.service.checkoutSetting;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.checkoutSetting.SettleAccountCustomer;
import com.taoding.mapper.checkoutSetting.SettleAccountCustomerDao;
/**
 * 企业结账启用禁用配置Service
 * 
 * @author mhb
 * @version 2017-12-25
 */
public interface SettleAccountCustomerService extends CrudService<SettleAccountCustomerDao,SettleAccountCustomer>{

	/**
	 * 根据账薄id获取信息(常规检测设置,其他异常检测) 
	 * @param bookId 账薄id
	 * @return
	 */

	/**
	 * 初始化插入企业结账启用禁用配置
	 * @param bookId 账薄id
	 * @param customerId 客户id
	 * @return
	 */
	public boolean initSave(String bookId,String customerId);
	/**
	 * 启用 禁用
	 * @param id 
	 * @return
	 */
	public boolean updateEnabled(String id);
	
	/**   
	* 根据账薄id获取信息常规检测设置 
	* @Description: TODO  
	* @param @param bookId
	* @param @return   
	* @return List<SettleAccountCustomer>   
	*/
	public List<SettleAccountCustomer> findGeneralTypeValidationListData(String bookId);
	
	/**   
	* 根据账薄id获取信息其他异常检测  
	* @Description: TODO  
	* @param @param bookId
	* @param @return   
	* @return List<SettleAccountCustomer>   
	*/
	public List<SettleAccountCustomer> findOtherTypeValidationListData(String bookId);
}
