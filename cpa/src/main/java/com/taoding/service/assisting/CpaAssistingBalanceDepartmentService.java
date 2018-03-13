
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;
import com.taoding.mapper.assisting.CpaAssistingBalanceDepartmentDao;

/**
 * 辅助核算部门Service
 * @author csl
 * @version 2017-11-20
 */
public interface CpaAssistingBalanceDepartmentService extends CrudService<CpaAssistingBalanceDepartmentDao, CpaAssistingBalanceDepartment> {

	/**
	 * 查询辅助核算类型下的部门信息
	 */
	public Object findAllList(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型详情中的部门条目
	 * @param queryMap
	 * @return
	 */
	public Object insertDepart(Map<String, Object> queryMap);
	
	/**
	 * 编辑部门的信息
	 * @param department
	 * @return
	 */
	public Object updateDepart(CpaAssistingBalanceDepartment department);
	
	/**
	 * 根据id删除部门
	 * @param id
	 * @return
	 */
	public Object deleteDepart(String id);
	
	/**
	 * 查询所有的部门
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-20 16:47:27
	 */
	public List<CpaAssistingBalanceDepartment> findAllList();
	
}