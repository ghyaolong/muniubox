package com.taoding.mapper.report.assetliability;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.assetliability.ReclassifySetting;

@Repository
@Mapper
public interface ReclassifySettingDao extends CrudDao<ReclassifySetting>{

	/**
	 * 向数据库中添加一个setting
	 * @param setting
	 * @return 影响的记录条数
	 */
	int saveSetting(ReclassifySetting setting);
	
	/**
	 * 更新一个setting
	 * @param setting
	 * @return 影响的记录条数
	 */
	int updateSetting(ReclassifySetting setting);
	
	/**
	 * 根据客户id获取配置项
	 * @param accountId
	 * @return ReclassifySetting instance
	 */
	ReclassifySetting findSettingByCustomerId(String customerId);
}
