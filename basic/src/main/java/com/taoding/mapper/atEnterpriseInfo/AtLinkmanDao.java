package com.taoding.mapper.atEnterpriseInfo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.atEnterpriseInfo.AtLinkman;

/**
 * 联系人信息DAO接口
 * @author csl
 * @version 2017-10-25
 */
@Repository
@Mapper
public interface AtLinkmanDao extends CrudDao<AtLinkman> {
	
	/**
	 * 根据atEnterpriseInfoId获取AtLinkman对象
	 * @param atEnterpriseInfoId
	 * @return
	 */
	public AtLinkman getByAtEnterpriseInfoId(String atEnterpriseInfoId);
}