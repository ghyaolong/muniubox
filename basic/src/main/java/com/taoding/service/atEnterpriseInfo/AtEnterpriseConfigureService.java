package com.taoding.service.atEnterpriseInfo;

import com.taoding.common.service.CrudService;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseConfigure;
import com.taoding.mapper.atEnterpriseInfo.AtEnterpriseConfigureDao;

/**
 * 企业配置信息Service
 * @author mhb
 * @version 2017-10-26
 */
public interface AtEnterpriseConfigureService extends CrudService<AtEnterpriseConfigureDao, AtEnterpriseConfigure> {

	/**
	 * 保存企业配置信息
	 */
	public void save(AtEnterpriseConfigure atEnterpriseConfigure); 	

	/**
	 * 根据企业id获取企业配置信息
	 */
	public AtEnterpriseConfigure findAtEnterpriseConfigureForAtEnterpriseInfoId(AtEnterpriseConfigure atEnterpriseConfigure);
 
	/**
	 * 上传图片完成后将图片路径保存到AtEnterpriseConfigure中
	 * @param atEnterpriseConfigure
	 */
	public int updateConfigure(AtEnterpriseConfigure atEnterpriseConfigure);
	
}