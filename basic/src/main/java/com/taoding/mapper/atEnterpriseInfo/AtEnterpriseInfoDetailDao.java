package com.taoding.mapper.atEnterpriseInfo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfoDetail;

/**
 * 企业详细信息管理DAO接口
 * @author csl
 * @version 2017-10-20
 */
@Repository
@Mapper
public interface AtEnterpriseInfoDetailDao extends CrudDao<AtEnterpriseInfoDetail> {
	
	/**
	 * 根据AtEnterpriseInfoId查找AtEnterpriseInfoDetail对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	public AtEnterpriseInfoDetail getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}