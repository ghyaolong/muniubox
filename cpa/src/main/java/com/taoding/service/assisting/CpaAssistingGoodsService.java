
package com.taoding.service.assisting;

import java.util.Map;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingGoods;
import com.taoding.mapper.assisting.CpaAssistingGoodsDao;

/**
 * 辅助核算模块存货信息表Service
 * @author csl
 * @version 2017-11-20
 */

public interface CpaAssistingGoodsService extends CrudService<CpaAssistingGoodsDao, CpaAssistingGoods> {

	/**
	 * 查询存货+分页
	 * @param queryMap
	 * @return
	 */
	public Object findAllList(Map<String, Object> queryMap);
	
	/**
	 * 新增辅助核算类型详情中的存货条目
	 * @param queryMap
	 * @return
	 */
	public Object insertGoods(Map<String, Object> queryMap);
	
	/**
	 * 编辑存货的信息
	 * @param cpaPosition
	 * @return
	 */
	public Object updateGoods(CpaAssistingGoods cpaAssistingGoods);

	/**
	 * 根据id删除存货的信息
	 * @param id
	 * @return
	 */
	public Object deleteGoods(String id);
	
	
}