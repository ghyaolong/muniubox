package com.taoding.service.atEnterpriseInfo;

import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfoDetail;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseInfoDetailDao;


/**
 * 企业详细信息管理Service
 * @author csl
 * @version 2017-10-20
 */
public interface AtEnterpriseInfoDetailService extends CrudService<AtEnterpriseInfoDetailDao, AtEnterpriseInfoDetail> {
	
	/**
	 * 根据AtEnterpriseInfoId查找AtEnterpriseInfoDetail对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	public AtEnterpriseInfoDetail getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}