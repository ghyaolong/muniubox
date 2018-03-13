package com.taoding.mapper.fixedAsset;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.fixedAsset.IntangibleAsset;

 
/**
 * 无形资产DAO接口
 * @author lixc
 * @version 2017-11-27
 */
@Repository
@Mapper
public interface IntangibleAssetDao extends CrudDao<IntangibleAsset> {
	/**
	 * 
	* @Description: TODO(按条件产寻IntangibleAssets 列表) 
	* { 
	* currentPeriod date 当前账期 yyyy-MM
	* accountId String 账薄ID
	* }
	* @return List<IntangibleAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public List<IntangibleAsset> findIntangibleAssetListByMap(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(无形资产单表查询) 
	* @param queryMap{
	* accountId 账薄ID
	* }
	* @return List<IntangibleAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	public List<IntangibleAsset>findSingleIntangibleAssetListByMap(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(求和) 
	* @param queryMap {groupby:分组}
	* @return List<IntangibleAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月8日
	 */
	public List<IntangibleAsset> sumIntangibleAssetListByMap(Map<String, Object> queryMap);
	/**
	 * 
	 * @Description: 查询此账薄下无形资产最大编号
	 * @param bookId
	 * @author MHB
	 * @date 2017年12月12日
	 */
	public String findBookMaxCode(String bookId);
}