package com.taoding.service.report.assetliability;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.assetliability.ReclassifySetting;
import com.taoding.mapper.report.assetliability.ReclassifySettingDao;

public interface ReclassifySettingService extends CrudService<ReclassifySettingDao, ReclassifySetting> {

	/**
	 * 保存
	 * @return 成功与否
	 */
	boolean saveReclassifySetting(ReclassifySetting reclassifySetting);
	
	/**
	 * 修改
	 * @param reclassifySetting
	 * @return boolean
	 */
	boolean updateReclassifySetting(ReclassifySetting reclassifySetting);
	
	/**
	 * 获取
	 * @param customerId
	 * @return ReclassifySetting实例
	 */
	ReclassifySetting findSettingByCustomerId(String customerId); 
}
