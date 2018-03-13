package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.domain.fixedAsset.LongApportionedAssets;

/**
 * 
 * 长期待摊
 * @author MHB
 * @date 2017年12月11日 下午2:25:54
 *
 */
public interface LongApportionedAssetsService {
	/**
	 * 
	 * 长期待摊资产保存
	 * @param LongApportionedAssets
	 * @return Object 返回类型
	 * @throws
	 * @author MHB
	 * @date 2017年12月11日
	 */
	void saveApportionedAssets(LongApportionedAssets longApportionedAssets);

	/**
	 * 
	 * 长期待摊资产删除
	 * @param ids
	 * @return Object 返回类型
	 * @throws
	 * @author MHB
	 * @date 2017年12月11日
	 */
	void batchDeleteApportionedAssets(String ids);

	/**
	 * 
	 * 列表查询
	 * 
	 * @return Object 返回类型
	 * @throws
	 * @author MHB
	 * @date 2017年12月11日
	 */
	PageInfo<LongApportionedAssets> findListByPage(Map<String, Object> queryMap);
	/**
	 * 
	 * 对账
	 * 
	 * @return accountId 账薄id
	 * @throws
	 * @author MHB
	 * @date 2017年12月11日
	 */
	Object reconciliation(String accountId);
	/**
	 * 
	* 获得客户默认的  摊销费用科目累计摊销科目
	* @param bookId
	* @return List<CpaCustomerSubject> 返回类型     {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public Map<String, Object> getFeeSubjectAndCumulationSubjectByBookId(String bookId);
	/**
	  * 
	 * 批量处理 
	 * @param ids
	 * @return Object 返回类型    
	 * @throws 
	 * @author lixc
	 * @date 2017年12月1日
	  */
	void batctHandedByIds(String ids);
	/**
	* 获得LongApportionedAssets
	* @param id
	* @return Object 返回类型    
	* @throws 
	* @author MHB
	* @date 2017年12月14日
	 */
	Object get(String id);
	/**
	 * 
	* 获得月摊销额  
	* @param originalValue 原值
	* @param lifecycle 使用期限(月)
	* @param depreciationType 月摊销额 
	* @return BigDecimal 返回类型    
	* @throws 
	* @author mhb
	* @date 2017年12月14日
	 */
	Object getDepreciationPerMonth(BigDecimal originalValue,Integer lifecycle, Integer depreciationType);

}
