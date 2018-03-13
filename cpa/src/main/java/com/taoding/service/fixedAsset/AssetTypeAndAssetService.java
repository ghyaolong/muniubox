package com.taoding.service.fixedAsset;


import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.AssetTypeAndAsset;

/**
 * 
* @ClassName: AssetTypeAndAssetService 
* @Description: TODO(资产 、 资产类型管理) 
* @author lixc 
* @date 2017年12月4日 下午2:25:54 
*
 */
public interface AssetTypeAndAssetService {

	/**
	* @Description: TODO(产寻返回 资产类型) 
	* @param accountId
	* @param type
	* @param assetName
	* @return AssetType 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public AssetType findAssetType(String accountId,int type,String assetName);
	
	
	/**
	 * 
	* @Description: TODO(检测 AssetTypeAndAsset) 
	* @param assetTypeAndAsset void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public void checkAssetTypeAndAsset(AssetTypeAndAsset assetTypeAndAsset) ;
	
}
