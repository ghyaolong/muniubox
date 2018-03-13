package com.taoding.service.fixedAsset;


import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.mapper.fixedAsset.AssetTypeDao;

public interface AssetTypeService extends CrudService<AssetTypeDao, AssetType> {

	/**
	 * 
	* @Description: TODO(通过账簿ID获得资产类型列表) 
	* @param accountId
	* @param type 1 固定 2 无形 
	* @return List<AssetType> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月27日
	 */
  public List<AssetType> findAssetTypeListByAccountId(String accountId,Integer type);
  
  /**
   * 
  * @Description: TODO(保存资产类型) 
  * @param assetType
  * @return int 返回类型    
  * @throws 
  * @author lixc
  * @date 2017年12月18日
   */
  public void saveAssetType (AssetType assetType);
}
