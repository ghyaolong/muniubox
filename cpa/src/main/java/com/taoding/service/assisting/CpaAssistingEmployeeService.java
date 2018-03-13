
package com.taoding.service.assisting;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingEmployee;
import com.taoding.mapper.assisting.CpaAssistingEmployeeDao;

/**
 * 辅助核算模块员工Service
 * @author csl
 * @version 2017-11-20
 */
public interface CpaAssistingEmployeeService extends CrudService<CpaAssistingEmployeeDao, CpaAssistingEmployee> {

	/**
	 * 查询员工的列表+分页
	 */
	public Object findAllList(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型详情中的员工条目
	 * @param queryMap
	 * @return
	 */
	public Object insertEmployee(Map<String, Object> queryMap);
	
	/**
	 * 编辑员工的信息
	 * @param employee
	 * @return
	 */
	public Object updateEmployee(CpaAssistingEmployee employee);
	
	/**
	 * 根据id删除员工
	 * @param id
	 * @return
	 */
	public Object deleteEmployee(String id);
	
	

}