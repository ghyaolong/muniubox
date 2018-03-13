package com.taoding.service.atEnterpriseInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.atEnterpriseInfo.AtLinkman;
import com.taoding.mapper.atEnterpriseInfo.AtLinkmanDao;


/**
 * 联系人信息Service
 * @author csl
 * @version 2017-10-25
 */
@Service
public class AtLinkmanServiceImpl extends DefaultCurdServiceImpl<AtLinkmanDao, AtLinkman>
	implements AtLinkmanService{

	/**
	 * 根据atEnterpriseInfoId获取AtLinkman对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	public AtLinkman getByAtEnterpriseInfoId(String atEnterpriseInfoId){
		return dao.getByAtEnterpriseInfoId(atEnterpriseInfoId);
	}
}