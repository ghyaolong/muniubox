package com.taoding.service.atEnterpriseInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfoDetail;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseInfoDetailDao;


/**
 * 企业详细信息管理Service
 * @author csl
 * @version 2017-10-20
 */
@Service
@Transactional
public class AtEnterpriseInfoDetailServiceImpl extends DefaultCurdServiceImpl<AtEnterpriseInfoDetailDao, AtEnterpriseInfoDetail>
	implements AtEnterpriseInfoDetailService{

	/**
	 * 根据AtEnterpriseInfoId查找AtEnterpriseInfoDetail对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	@Override
	public AtEnterpriseInfoDetail getByAtEnterpriseInfoId(String atEnterpriseInfoId) {
		return dao.getByAtEnterpriseInfoId(atEnterpriseInfoId);
	}
	
	
}