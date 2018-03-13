
package com.taoding.service.assisting;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.assisting.CpaAssistingTemplate;
import com.taoding.mapper.assisting.CpaAssistingTemplateDao;

/**
 * 辅助核算类型模板Service
 * @author CSL
 * @version 2017-11-17
 */
public interface CpaAssistingTemplateService extends CrudService<CpaAssistingTemplateDao, CpaAssistingTemplate> {

	/**
	 * 查询全部模板数据
	 * 2017年11月22日 下午2:24:06
	 * @return
	 */
	public List<CpaAssistingTemplate> findAllList();
	
}