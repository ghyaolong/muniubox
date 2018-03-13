
package com.taoding.service.assisting;

import java.util.List;
import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.mapper.assisting.CpaAssistingExpenseTypeDao;

/**
 * 辅助核算模块的费用类型Service
 * @author csl
 * @version 2017-11-20
 */
public interface CpaAssistingExpenseTypeService extends CrudService<CpaAssistingExpenseTypeDao, CpaAssistingExpenseType> {

	/**
	 * 查询所有的费用
	 * @param cpaAssistingExpenseType
	 * @return
	 * add csl
	 * 2017-11-20 16:47:27
	 */
	public List<CpaAssistingExpenseType> findAllList();
	
}