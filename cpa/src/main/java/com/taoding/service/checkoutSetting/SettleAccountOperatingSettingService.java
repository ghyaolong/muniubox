package com.taoding.service.checkoutSetting;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingSetting;
import com.taoding.mapper.checkoutSetting.SettleAccountOperatingSettingDao;
/**
 * 经营数据分析设置Service
 * 
 * @author mhb
 * @version 2017-12-25
 */
public interface SettleAccountOperatingSettingService extends CrudService<SettleAccountOperatingSettingDao,SettleAccountOperatingSetting>{

	/**
	 * 初始化插入经营数据分析设置
	 * @param bookId 账薄id
	 * @param customerId 客户id
	 * @return
	 */
	public Object initSave(String bookId,String customerId);
	
	/**
	 * 查询经营数据分析设置
	 * @param id 
	 * @return List<SettleAccountOperatingSetting>
	 */
	public List<SettleAccountOperatingSetting> findSettleAccountOperatingListData(String id);

}
