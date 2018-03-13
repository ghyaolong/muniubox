
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.mapper.assisting.CpaCustomerAssistingDao;


/**
 * 辅助核算类型Service
 * @author csl
 * @version 2017-11-16
 */
public interface CpaCustomerAssistingService extends CrudService<CpaCustomerAssistingDao, CpaCustomerAssisting> {
	
	/**
	 * 查询所有的辅助核算类型列表
	 * @param accountId
	 * @return
	 * add csl
	 * 2017-11-23 09:08:07
	 */
	public List<CpaCustomerAssisting> findList(String accountId);

	/**
	 * 增加辅助核算类型中的自定义分类
	 * @param cpaCustomerAssisting
	 * @return
	 */
	public Object insertCustom(CpaCustomerAssisting cpaCustomerAssisting);
	
	/**
	 * 根据科目id,查询科目下所有辅助核算信息
	 * 2017年11月22日 下午4:43:04
	 * @param subjectId
	 * @return
	 */
	public List<CpaCustomerAssisting> findListBySubjectId(String subjectId);

	/**
	 * 根据id和custom删除自定义分类
	 * @param id
	 * @return
	 */
	public Object deleteCustom(String id);

	/**
	 * 初始化辅助核算模板数据
	 * @param customerId
	 */
	public void initAssisting(String customerId);
	
	/**
	 * 新增辅助核算类型的详情条目
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-18 15:18:16
	 */
	public Object insertAllDetail(Map<String,Object> maps);
	
	/**
	 * 根据辅助核算类型的id查找相应的列表
	 * @param maps
	 * @returns
	 */
	public Object findTypeListById(Map<String,Object> maps);
}