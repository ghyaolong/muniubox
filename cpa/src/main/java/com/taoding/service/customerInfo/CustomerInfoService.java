package com.taoding.service.customerInfo;

import java.util.List;
import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.customerInfo.CpaCustomerLinkman;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.mapper.customerInfo.CustomerInfoDao;

/**
 * 客户管理详请Service
 * 
 * @author mhb
 * @version 2017-11-16
 */
public interface CustomerInfoService extends
		CrudService<CustomerInfoDao, CustomerInfo> {

	/**
	 * 根据客户id查询客户信息、联系人、签约信息
	 * 
	 * @param id
	 *            客户id
	 * @return
	 */
	Object findCoutomerInfo(String id);

	/**
	 * 添加公司信息
	 * 
	 * @param customerInfo
	 * @return
	 */
	Object saveCustomerInfo(CustomerInfo customerInfo);

	/**
	 * 根据id删除公司信息
	 * 
	 * @param id
	 *            公司id
	 * @return
	 */
	Object deleteCustomerInfo(String id);

	/**
	 * 修改客户信息
	 * 
	 * @param customerInfo
	 *            客户信息实体类
	 * @return
	 */
	Object editCustomerInfo(CustomerInfo customerInfo);

	/**
	 * 添加公司联系人信息
	 * 
	 * @param customerInfo
	 * @return
	 */
	Object saveCustomerLinkman(CpaCustomerLinkman cpaCustomerLinkman);

	/**
	 * 根据公司联系人id删除联系人 逻辑删除
	 * 
	 * @param id
	 *            公司id
	 * @return
	 */
	Object deleteCustomerLinkman(String id);

	/**
	 * 修改客户 联系人信息
	 * 
	 * @param cpaCustomerLinkman
	 *            客户联系人实体类
	 * @return
	 */
	Object editCustomerLinkman(CpaCustomerLinkman cpaCustomerLinkman);
    /**
     * 分页查询
     * @author lixc
     * @param queryMap 查询条件
     * @date 2017年11月17日11:46:00
     */
	public List<CustomerInfo> findCustomInfoListByMap(Map<String, Object> queryMap); 
	/**
	 * 获取客户编号
	 * @return
	 * @author mhb
	 */
	Object getNextNo();
	/**
	 * 启用禁用
	 * @param id
	 * @return
	 * @author mhb
	 */
	boolean updateEnable(String id);
	
//	/**
//	 * 
//	* @Description: TODO(建账) 
//	* @param custormId void 返回类型    
//	* @throws 
//	* @author lixc
//	* @date 2017年12月4日
//	 */
//	public void prepareAccountsBook(String custormId);
}
