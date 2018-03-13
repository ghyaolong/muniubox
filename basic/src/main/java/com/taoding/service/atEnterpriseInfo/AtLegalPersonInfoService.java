package com.taoding.service.atEnterpriseInfo;

import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtLegalPersonInfo;
import com.taoding.mapper.atEnterpriseInfo.AtLegalPersonInfoDao;


/**
 * 法人信息Service
 * @author Lin
 * @version 2017-09-28
 */
public interface AtLegalPersonInfoService extends CrudService<AtLegalPersonInfoDao, AtLegalPersonInfo> {

	public AtLegalPersonInfo getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}