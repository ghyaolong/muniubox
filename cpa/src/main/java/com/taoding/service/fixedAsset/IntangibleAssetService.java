package com.taoding.service.fixedAsset;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.taoding.domain.fixedAsset.IntangibleAsset;

public interface IntangibleAssetService {

	/**
	 * 
	* @Description: TODO(保存无形资产) 
	* @param IntangibleAsset
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public void saveIntangibleAsset(IntangibleAsset intangibleAsset);
	
	/**
	 * 
	* @Description: TODO(通过查询条件获得分页) 
	* @param queryMap
	* {pageNo	int	否	分页页码
	* pageSize	int	否	每页显示多少条
	* isAll   boolean 是否全查询
	* currentPeriod date 当前账期
	* accountId String 账薄ID
	* }
	* @return PageInfo<IntangibleAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public PageInfo<IntangibleAsset> findIntangibleAssetByPage(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(批量删除无形资产) 
	* @param ids
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public int deleteIntangibleAssetByIds(String ids);
	
	/**
	 * 
	* @Description: TODO(验证折旧方式的合法性) 
	* @param IntangibleAsset
	* @param period 账期
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月30日
	 */
	public boolean checkDepreciationType(IntangibleAsset intangibleAsset,Date period);
	
	/**
	* @Description: TODO(对账) 
	* @param accountId 账簿ID
	* @return Map<String,Object> 
	* {isEquilibria:true,//是否平衡
	* resourcesOriginal :1231,//资源原值
	* assetsAccumulatedDepreciation:21231,//资产累计折旧
	* intangibleAssets:1212,//无形资产
	* accumulatedDepreciation:23423 //累计折旧
	* } 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	public Map<String, Object> reconciliation(String accountId);
	
	/**
	 * 
	* @Description: TODO(批量处理) 
	* @param ids
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public int batctHandedByIds(String ids);
	
	/**
	 * 
	* @Description: TODO(保存) 
	* @param intangibleAsset void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月8日
	 */
	public  void  save(IntangibleAsset intangibleAsset);
	
	/**
	 * 
	* @Description: TODO(通过ID 获得 IntangibleAsset) 
	* @param id
	* @return IntangibleAsset 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月8日
	 */
	public IntangibleAsset get(String id);
	
	/**
	 * 
	* @Description: TODO(获得客户默认的  折旧费用科目 累计折旧科目) 
	* @param customerInfoId
	* @return List<CpaCustomerSubject> 返回类型     {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public Map<String, Object> getFeeSubjectAndCumulationSubjectByBookId(String customerInfoId);

	
	/**
	 * 
	* @Description: TODO(获得月摊销额) 
	* @param originalValue
	* @param originalDepreciation
	* @param lifecycle
	* @param depreciationType
	* @param period
	* @param type
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月12日
	 */
	public BigDecimal getDepreciationPerMonth(
			String accountBookId,
			BigDecimal originalValue , 
			BigDecimal originalDepreciation,
			Integer lifecycle ,
			Integer depreciationType);
	
	
	/**
	 * 
	* @Description: TODO(清理、处理 资产) 
	* @param 继承 fixedAsset 的实体类
	* @param type 1 固定 2 无形 3 长期摊销
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月13日
	 */
	public boolean  HandedBy(IntangibleAsset fixedAsset,int type);
}
