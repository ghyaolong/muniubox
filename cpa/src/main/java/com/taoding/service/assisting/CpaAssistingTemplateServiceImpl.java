
package com.taoding.service.assisting;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.assisting.CpaAssistingTemplate;
import com.taoding.mapper.assisting.CpaAssistingTemplateDao;

/**
 * 辅助核算类型模板Service
 * @author CSL
 * @version 2017-11-17
 */
@Service
@Transactional
public class CpaAssistingTemplateServiceImpl 
	extends DefaultCurdServiceImpl<CpaAssistingTemplateDao, CpaAssistingTemplate>
	implements CpaAssistingTemplateService{

	
	/**
	 * 查询全部模板数据
	 * 2017年11月22日 下午2:24:06
	 * @return
	 */
	@Override
	public List<CpaAssistingTemplate> findAllList() {
		return dao.findAllList();
	}
	
	
	
}