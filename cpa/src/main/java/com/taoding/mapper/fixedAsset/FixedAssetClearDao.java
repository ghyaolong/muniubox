package com.taoding.mapper.fixedAsset;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.fixedAsset.FixedAsset;

 
/**
 * 固定资产清理DAO接口
 * @author mhb
 * @version 2017-12-15
 */
@Repository
@Mapper
public interface FixedAssetClearDao extends CrudDao<FixedAsset> {
	/**
	 * 
	 * 固定资产清理单表查询
	 * 
	 * @param queryMap
	 *            { accountId 账薄ID }
	 * @return List<FixedAsset> 返回类型
	 * @throws
	 * @author mhb
	 * @date 2017年12月15日
	 */
	List<FixedAsset> findFixedAssetClearListByMap(Map<String, Object> queryMap);
	/**
	 * 
	 * 固定资产清理还原
	 * 
	 * @param fixedAsset
	 * @return  
	 * @throws
	 * @author mhb
	 * @date 2017年12月15日
	 */
	int updateStatus(FixedAsset fixedAsset);
	 
}