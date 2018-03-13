
package com.taoding.service.assisting;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingProject;
import com.taoding.mapper.assisting.CpaAssistingProjectDao;

/**
 * 辅助核算模块项目信息表Service
 * @author csl
 * @version 2017-11-20
 */
public interface CpaAssistingProjectService extends CrudService<CpaAssistingProjectDao, CpaAssistingProject> {

	/**
	 * 查询辅助核算类型下的项目信息
	 */
	public Object findAllList(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型详情中的项目条目
	 * @param queryMap
	 * @return
	 */
	public Object insertProject(Map<String, Object> queryMap);
	
	/**
	 * 编辑项目的信息
	 * @param cpaPosition
	 * @return
	 */
	public Object updateProject(CpaAssistingProject cpaAssistingProject);

	/**
	 * 根据id删除项目的信息
	 * @param id
	 * @return
	 */
	public Object deleteProject(String id);
	
}