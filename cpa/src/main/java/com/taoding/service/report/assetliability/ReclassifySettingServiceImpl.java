package com.taoding.service.report.assetliability;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.assetliability.ReclassifySetting;
import com.taoding.mapper.report.assetliability.ReclassifySettingDao;

@Service
@Transactional
public class ReclassifySettingServiceImpl extends DefaultCurdServiceImpl<ReclassifySettingDao, ReclassifySetting> implements ReclassifySettingService{

	@Override
	public boolean saveReclassifySetting(ReclassifySetting reclassifySetting) {
		return dao.saveSetting(reclassifySetting) > 0 ? true : false;
	}

	@Override
	public boolean updateReclassifySetting(ReclassifySetting reclassifySetting) {
		return dao.updateSetting(reclassifySetting) > 0 ? true : false;
	}

	@Override
	public ReclassifySetting findSettingByCustomerId(String customerId) {
		return dao.findSettingByCustomerId(customerId);
	}

}
