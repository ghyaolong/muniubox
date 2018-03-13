
package com.taoding.service.assisting;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingPosition;
import com.taoding.mapper.assisting.CpaAssistingPositionDao;

/**
 * 职位Service
 * @author csl
 * @version 2017-11-22
 */
public interface CpaAssistingPositionService extends CrudService<CpaAssistingPositionDao, CpaAssistingPosition> {

	/**
	 * 查询辅助核算类型下的职位信息
	 */
	public Object findAllList(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型详情中的职位条目
	 * @param queryMap
	 * @return
	 */
	public Object insertPosition(Map<String, Object> queryMap);
	
	/**
	 * 编辑职位的信息
	 * @param cpaPosition
	 * @return
	 */
	public Object updateCpaPosition(CpaAssistingPosition cpaPosition);
	
	/**
	 * 根据id删除职位
	 * @param cpaPosition
	 * @return
	 */
	public Object deleteCpaPosition(String id);
	
	/**
	 * 查询所有的职位(包含模糊搜索)
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-20 16:47:27
	 */
	public List<CpaAssistingPosition> findAllList();
	

	
}