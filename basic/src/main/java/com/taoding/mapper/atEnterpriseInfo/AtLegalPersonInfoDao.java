package com.taoding.mapper.atEnterpriseInfo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.atEnterpriseInfo.AtLegalPersonInfo;

/**
 * 法人信息DAO接口
 * @author Lin
 * @version 2017-09-28
 */
@Repository
@Mapper
public interface AtLegalPersonInfoDao extends CrudDao<AtLegalPersonInfo> {
	
	/**
	 * 根据AtEnterpriseInfoId查找AtLegalPersonInfo对象
	 * @param
	 * @return
	 */
	public AtLegalPersonInfo getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}