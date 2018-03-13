
package com.taoding.mapper.atEnterpriseInfo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseConfigure;

/**
 * 企业配置信息DAO接口
 * @author mhb
 * @version 2017-10-26
 */
@Repository
@Mapper
public interface AtEnterpriseConfigureDao extends CrudDao<AtEnterpriseConfigure> {
	
	public AtEnterpriseConfigure findAtEnterpriseConfigureForAtEnterpriseInfoId(AtEnterpriseConfigure atEnterpriseConfigure);
	
	/**
	 * 上传图片完成后将图片路径保存到AtEnterpriseConfigure中
	 */
	public int updateConfigure(AtEnterpriseConfigure atEnterpriseConfigure);
	
	/**
	 * 根据atEnterpriseInfoId获取AtEnterpriseConfigure对象 
	 * @param atEnterpriseConfigure
	 * @return
	 */
	public AtEnterpriseConfigure getConfigure(AtEnterpriseConfigure atEnterpriseConfigure);
}