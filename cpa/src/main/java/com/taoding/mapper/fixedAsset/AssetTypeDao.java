package com.taoding.mapper.fixedAsset;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.fixedAsset.AssetType;

/**
 * 固定资产DAO接口
 * @author lixc
 * @version 2017-11-27
 */
@Repository
@Mapper
public interface AssetTypeDao extends CrudDao<AssetType> {
	
	public String findMaxNoByAccountIdAndType(@Param("accountId")String accountId ,@Param("type")Integer type);
}