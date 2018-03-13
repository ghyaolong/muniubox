package com.taoding.service.subject;

import com.taoding.common.service.CrudService;
import com.taoding.domain.subject.CpaSubjectFormatSetting;
import com.taoding.mapper.subject.CpaSubjectFormatSettingDao;

/**
 * 科目Excel导入设置
 * @author fc
 */
public interface CpaSubjectFormatSettingService extends CrudService<CpaSubjectFormatSettingDao, CpaSubjectFormatSetting>{

	/**
	 * 保存科目Excel导入设置
	 * @author fc 
	 * @version 2017年12月20日 上午9:31:26 
	 * @param csfs
	 * @return
	 */
	public boolean saveSubjectSetting(CpaSubjectFormatSetting csfs);
	
	/**
	 * 根据客户id获取Excel导入设置详情
	 * @author fc 
	 * @version 2017年12月20日 上午9:38:02 
	 * @param id
	 * @return
	 */
	public CpaSubjectFormatSetting getSettingByCustomerId(String customerId);
	
	/**
	 * 删除Excel导入设置
	 * @author fc 
	 * @version 2017年12月20日 上午10:03:07 
	 * @return
	 */
	public boolean delSettingById(String id);
}
