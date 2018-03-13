package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;

/**
 * 
 * @ClassName: fixedAssetTool
 * @Description: TODO(资产计算应用类)
 * @author lixc
 * @date 2017年12月1日 下午3:44:12
 */
public interface FixedAssetToolService {


	// 求和资源原值
	public BigDecimal originalVlaueSum(List< ? extends  FixedAsset> list) ;

	
	/**
	 * 
	* @Description: TODO(工作账薄ID获得客户Id) 
	* @param accountId
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getCustomerIdByAccountId(String accountId);
	
	/**
	 * @Description: TODO(科目固定资产)
	 * @param accountId
	 *            账薄ID
	 * @param subNo 科目编号
	 * @return BigDecimal 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月1日
	 */
	public BigDecimal fixedAsset(String accountId,String subNo) throws LogicException ;

	/**
	 * 
	* @Description: TODO(科目累计折旧额) 
	* @param list
	* @param period 账期
	* @param accountId 账薄ID
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	
	public BigDecimal accumulatedDepreciation(List< ? extends  FixedAsset> list,
			String accountId) ;
	/**
	 * 
	* @Description: TODO(通过固定资产获得凭证科目) 
	* @param fixedAsset
	* @param period 账期
	* @param type 1 固定 2 无形
	* @return CpaVoucherSubject 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public void getVoucherSubjectByFixedAsset(FixedAsset fixedAsset,
			CpaVoucher voucher,Date period,int type) ;

	/**
	 * 
	* @Description: TODO(获得固定资产 科目ID) 
	* @param 账簿ID
	* @param 科目编码
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getFixedAssetSubjectId(String bookId,String code);
	/**
	 * 
	* @Description: TODO(获得清理固定资产 科目ID) 
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getFixedAssetClearSubjectId(String customerId);
	
	/**
	* @Description: TODO(资产bean验证) 
	* @param entity
	* @param type 资产类型     1 固定 2 无形
	* @throws 
	* @author lixc
	* @date 2017年12月8日
	 */
	public void validataAssetBean(FixedAsset entity ,int type);
	
	/**
	 * 
	* @Description: TODO(资产累计折旧额 初期累计折旧额 + (开始做账到现在的账期 +1 期后求和)) 
	* @param list
	* @param accountId
	* @param period
	* @param type 1 固定 2 无形
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月7日
	 */
	public BigDecimal depreciationPerMonthSum(List< ? extends  FixedAsset> list,int type);
	
	/**
	 * 
	* 获取净值(净值=原值-期初累计摊销额值-月摊销额) 
	* @param originalValue  原值
	* @param originalDepreciation 期初累计摊销额值
	* @param depreciationPerMonth  月摊销额
	* @author mhb
	* @date 2017年12月12日
	 */
	public BigDecimal netWorth(BigDecimal originalValue,BigDecimal originalDepreciation,BigDecimal depreciationPerMonth);
	/**
	 * 
	* 获取编号 
	* @param bookId 账薄id
	* @param assetType 资产类型 1 固定资产 2 无形资产 3待摊资产
	* @author mhb
	* @date 2017年12月12日
	 */
	public String nextCode(String bookId,Integer assetType);
	
	/**
	* @Description: TODO(当前账期 结账获得计提，待摊，长期待摊 点击后展示科目列表) 
	* @param bookId 账薄ID
	* @param abstractsName 摘要名称
	* @param type 1 固定  2 无形 3 长期摊销
	* @param isDebit true 是 false 否 是否借方
	* @return List<CpaVoucherSubject> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月21日
	 */
	public List<CpaVoucherSubject> getSubjectAndSumthisMonthDepreciation(String bookId,int type,boolean isDebit,String abstractsName);

	/**
	 * 
	* @Description:更改原有资产状态  为 
	* @param list
	* @param status 状态
	* @param type  1 固定 2无形 3 长期摊销
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月28日
	 */
	public boolean updateStatusByAssetList(List<? extends FixedAsset> list, Integer status,Integer type);
	/**
	 * 
	* @Description:批量插入
	* @param list
	* @param type  1 固定 2无形 3 长期摊销
	* @return boolean 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月28日
	 */
	public boolean batchInsertAssetList(List<? extends FixedAsset> list, Integer type);
}