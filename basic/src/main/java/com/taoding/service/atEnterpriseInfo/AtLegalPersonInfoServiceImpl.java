package com.taoding.service.atEnterpriseInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.atEnterpriseInfo.AtLegalPersonInfo;
import com.taoding.mapper.atEnterpriseInfo.AtLegalPersonInfoDao;


/**
 * 法人信息Service
 * @author Lin
 * @version 2017-09-28
 */
@Service
@Transactional
public class AtLegalPersonInfoServiceImpl extends DefaultCurdServiceImpl<AtLegalPersonInfoDao, AtLegalPersonInfo>
	implements AtLegalPersonInfoService{
	/**
	 * 根据AtEnterpriseInfoId查找AtLegalPersonInfo对象
	 * @param
	 * @return
	 */
	@Override
	public AtLegalPersonInfo getByAtEnterpriseInfoId(String atEnterpriseInfoId) {
		return dao.getByAtEnterpriseInfoId(atEnterpriseInfoId);
	}
	
	
}