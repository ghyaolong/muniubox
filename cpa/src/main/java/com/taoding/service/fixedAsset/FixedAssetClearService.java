package com.taoding.service.fixedAsset;


import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.mapper.fixedAsset.FixedAssetClearDao;

public interface FixedAssetClearService extends CrudService<FixedAssetClearDao, FixedAsset> {
	/**
	 * 
	* 通过查询条件获得分页 
	* @param queryMap
	* @return PageInfo<LongApportionedAssets> 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年11月29日
	 */
	PageInfo<FixedAsset> findFixedAssetClearListByPage(Map<String, Object> queryMap);

	Object restore(String ids);

 
}
