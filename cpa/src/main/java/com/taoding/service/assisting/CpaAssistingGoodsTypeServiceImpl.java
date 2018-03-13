
package com.taoding.service.assisting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.assisting.CpaAssistingGoodsType;
import com.taoding.mapper.assisting.CpaAssistingGoodsTypeDao;

/**
 * 辅助核算模块的存货类型Service
 * @author csl
 * @version 2017-11-20
 */
@Service
@Transactional
public class CpaAssistingGoodsTypeServiceImpl extends DefaultCurdServiceImpl<CpaAssistingGoodsTypeDao, CpaAssistingGoodsType>
	implements CpaAssistingGoodsTypeService{
	
}