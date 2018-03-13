package com.taoding.mapper.fixedAsset;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.AssetTypeAndAsset;
import com.taoding.domain.fixedAsset.FixedAsset;

 
/**
 * 固定资产DAO接口
 * @author lixc
 * @version 2017-11-27
 */
@Repository
@Mapper
public interface FixedAssetDao extends CrudDao<FixedAsset> {
	/**
	 * 
	* @Description: TODO(按条件产寻FixedAsset 列表) 
	* { 
	* currentPeriod date 当前账期 yyyy-MM
	* accountId String 账薄ID
	* }
	* @return List<FixedAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public List<FixedAsset> findFixedAssetListByMap(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(固定资产单表查询) 
	* @param queryMap{
	* accountId 账薄ID
	* }
	* @return List<FixedAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	public List<FixedAsset>findSingleFixedAssetListByMap(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(插入 assetTypeAndAsset) 
	* @param assetTypeAndAsset
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public int insertAssetTypeAndAsset(AssetTypeAndAsset assetTypeAndAsset);
	
	/**
	* @Description: TODO() 
	* @param assetTypeAndAsset
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public int updateAssetTypeAndAsset(AssetTypeAndAsset assetTypeAndAsset);
	
	/**
	* @Description: TODO() 
	* @param assetName
	* @param accountId
	* @param type
	* @return List<AssetTypeAndAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public List<AssetTypeAndAsset> findAssetTypeAndAssetList(@Param("assetName")String assetName ,
			@Param("accountId")String accountId, 
			@Param("type") Integer type,
			@Param("assetTypeId")String assetTypeId);
	/**
	 * 
	* @Description: TODO(求和) 
	* @param queryMap {groupby:分组}
	* @return List<IntangibleAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月8日
	 */
	public List<FixedAsset> sumFixedAssetListByMap(Map<String, Object> queryMap);
	
	/**
	* @Description: TODO(查询名称) 
	* @param queryMap{
	* periodFrom
	* periodTo
	* accountId
	* assetTypeId
	* type
	* }
	* @return List<Map<String,Object>> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月13日
	 */
	public List<Map<String, Object>> findAssetNameList(Map<String, Object> queryMap);

	/**
	 * 
	 * @Description: 查询此账薄下固定资产最大编号
	 * @param bookId
	 * @author MHB
	 * @date 2017年12月12日
	 */
	public String findBookMaxCode(String bookId);
	
	/**
	 * 
	* @Description: TODO(批量更新) 
	* @param list
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月19日
	 */
	public int updateBatchFixedAssetByVourcherIds(List<FixedAsset> list);
	
	/**
	 * 
	* @Description:批量插入 assetType from cpa_asset_type_template
	* @param assetType
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月22日
	 */
	public int insertAssetTypeByAssetTypeTemplate(AssetType assetType);

}