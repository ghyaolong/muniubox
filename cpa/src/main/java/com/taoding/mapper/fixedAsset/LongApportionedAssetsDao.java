package com.taoding.mapper.fixedAsset;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.fixedAsset.LongApportionedAssets;

/**
 * 长期待摊DAO接口
 * 
 * @author mhb
 * @version 2017-12-11
 */
@Repository
@Mapper
public interface LongApportionedAssetsDao extends CrudDao<LongApportionedAssets> {
	/**
	 * 
	 * @Description: 长期待摊资产批量删除
	 * @param array   ids
	 * @author MHB
	 * @date 2017年12月12日
	 */
	void batchUpdate(@Param("ids") String[] ids);
	/**
	 * 
	 * @Description: 长期待摊资产列表查询
	 * @param queryMap
	 * @author MHB
	 * @date 2017年12月12日
	 */
	List<LongApportionedAssets> findListByPage(Map<String, Object> queryMap);
	/**
	 * 
	 * @Description: 查询合计
	 * @param queryMap
	 * @author MHB
	 * @date 2017年12月12日
	 */
	List<LongApportionedAssets> sumLongApportionedAssetsListByMap( Map<String, Object> queryMap);
	/**
	 * 
	 * @Description: 查询此账薄下待摊资产最大编号
	 * @param bookId
	 * @author MHB
	 * @date 2017年12月12日
	 */
	String findBookMaxCode(String bookId);
	
	/**
	 * 
	* @Description: TODO(查询摊销单表，没有关联) 
	* @param queryMap
	* @return List<LongApportionedAssets> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月21日
	 */
	List<LongApportionedAssets> findLongApportioneAssetListByMap(Map<String, Object> queryMap);

}