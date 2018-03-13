package com.taoding.service.fixedAsset;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;


import com.github.pagehelper.PageInfo;
import com.taoding.common.service.CrudService;
import com.taoding.domain.fixedAsset.DepreciationDetail;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.mapper.fixedAsset.FixedAssetDao;

public interface FixedAssetService extends CrudService<FixedAssetDao, FixedAsset> {

	/**
	 * 
	* @Description: TODO(保存固定资产) 
	* @param fixedAsset
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public void saveFixedAsset(FixedAsset fixedAsset);
	
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
	* @return PageInfo<FixedAsset> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public PageInfo<FixedAsset> findFixedAssetListByPage(Map<String, Object> queryMap);
	
	/**
	 * 
	* @Description: TODO(批量删除固定资产) 
	* @param ids
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月29日
	 */
	public int deleteFixedAssetByIds(String ids);
	
	/**
	 * 
	* @Description: TODO(验证折旧方式的合法性) 
	* @param fixedAsset
	* @param period 账期
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月30日
	 */
	public boolean checkDepreciationType(FixedAsset fixedAsset,Date period);
	
	/**
	* @Description: TODO(对账) 
	* @param accountId 账簿ID
	* @return Map<String,Object> 
	* {isEquilibria:true,//是否平衡
	* resourcesOriginal :1231,//资源原值
	* assetsAccumulatedDepreciation:21231,//资产累计折旧
	* fixedAssets:1212,//固定资产
	* accumulatedDepreciation:23423 //累计折旧
	* } 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	public Map<String, Object> reconciliation(String accountId);
	
	/**
	 * 
	* @Description: TODO(批量清除) 
	* @param ids
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public int batctAccumulatedByIds(String ids);
	
	/**
	 * 
	* @Description: TODO(获得客户默认的  折旧费用科目 累计折旧科目) 
	* @param bookId
	* @return List<CpaCustomerSubject> 返回类型     {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	* @throws 
	* @author lixc
	* @date 2017年12月4日
	 */
	public Map<String, Object> getFeeSubjectAndCumulationSubjectBybookId(String bookId);

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
	public boolean  HandedBy(FixedAsset fixedAsset,int type);
	
	/**
	 * 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param depreciationStartDate
	* @param originalValue
	* @param originalDepreciation
	* @param lifecycle
	* @param depreciationType
	* @param period
	* @param residualRate
	* @param type
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月12日
	 */
	public BigDecimal getDepreciationPerMonth(
			Date depreciationStartDate,
			BigDecimal originalValue , 
			BigDecimal originalDepreciation,
			Integer lifecycle ,
			Integer depreciationType,
			Date period,
			Double residualRate,
			Integer type);

	/**
	 * 
	* @Description: TODO(获得资产下拉列表) 
	* @param poriodFrom
	* @param poriodTo
	* @param accountId
	* @param type
	* @param assetTypeId
	* @return List<Map<String,Object>> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月13日
	 */
	public List<Map<String, Object>> findAssetNameList( Date poriodFrom,  Date poriodTo, String accountId, Integer type,
			  String assetTypeId
			   );
	
	/**
	 * 
	* @Description: TODO(获得) 
	* @param bookId 账薄id
	* @param codes 编码数组
	* @param periodFrom 开始账期
	* @param periodTo 截止账期
	* @param assetType 资产类别
	* @param type 1 固定 2 无形 3 长期摊销
	* @return List<DepreciationDetail> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月14日
	 */
	public List<DepreciationDetail> findAssetListByCodesAndPeriodAndType(
			 String bookId,
			 String periodFromStr,
			 String periodToStr,
			 String assetType,
			 Integer type,
			 List<String> codes);
	
	/**
	 * 
	* @Description: TODO(批量修改状态 固定资产状态) 
	* @param VoucherIds
	* @param 0 正常  1 已清理     2已结账 
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月19日
	 */
	public int  updateBatchStatusByVoucherIds(String [] VoucherIds,int status);
	
	/**
	* @Description: TODO(当前账期 结账获得计提，待摊，长期待摊 本月折旧求和数量) 
	* @param bookId 账薄ID
	* @param type 1 固定  2 无形 3 长期摊销
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月20日
	 */
	public BigDecimal sumThisMonthDepreciation(String bookId,int type);
	
	/**
	* @Description: TODO(当前账期 结账获得计提，待摊，长期待摊 点击后展示凭证) 
	* @param bookId 账薄ID
	* @param abstractsName 摘要名称
	* @param type 1 固定  2 无形 3 长期摊销
	* @return List<CpaVoucherSubject> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月21日
	 */
	public List<CpaVoucherSubject> getSubjectAndSumthisMonthDepreciation(String bookId,int type,String abstractsName);
	
	/**
	 * 
	* @Description: 初始化资产类型 
	* @param bookId void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月22日
	 */
	public int initAssetTypeFromAssetTypeTemplateBybookId(String bookId);
	
	/**
	 * 
	* @Description:资产 结账  用于更改账薄状态之前
	* @param bookId 账薄ID
	* @param type 1固定 2 无形 3 长期待摊
	* @param 是否更改账薄状态之前调用 前true 后 fase
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月28日
	 */
	public boolean  AssetInvoicing(String bookId,Integer type,boolean beforeChangeBookStatus) throws LoginException;
	/**
	 * 
	* 固定资产导入
	* @param bookId 账薄id
	* @param list 导入数据    
	* @author mhb
	* @date 2017年12月27日
	* @return
	 */
	public Map<String, Object> importListData(List<FixedAsset> list, String bookId);
 }
