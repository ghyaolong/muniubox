package com.taoding.service.atEnterpriseInfo;

import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtLinkman;
import com.taoding.mapper.atEnterpriseInfo.AtLinkmanDao;


/**
 * 联系人信息Service
 * @author csl
 * @version 2017-10-25
 */
public interface AtLinkmanService extends CrudService<AtLinkmanDao, AtLinkman> {
	/**
	 * 根据atEnterpriseInfoId获取AtLinkman对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	public AtLinkman getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}