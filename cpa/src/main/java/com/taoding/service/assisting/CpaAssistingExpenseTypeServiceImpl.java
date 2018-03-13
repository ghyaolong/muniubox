
package com.taoding.service.assisting;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.mapper.assisting.CpaAssistingExpenseTypeDao;

/**
 * 辅助核算模块的费用类型Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingExpenseTypeServiceImpl 
	extends DefaultCurdServiceImpl<CpaAssistingExpenseTypeDao, CpaAssistingExpenseType> 
	implements CpaAssistingExpenseTypeService{

	@Override
	public List<CpaAssistingExpenseType> findAllList() {
		return dao.findAllList();
	}
	
}