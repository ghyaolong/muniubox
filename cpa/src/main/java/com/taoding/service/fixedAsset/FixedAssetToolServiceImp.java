package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.MathUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.IntangibleAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.domain.vouchersummary.CpaVoucherSummary;
import com.taoding.mapper.fixedAsset.FixedAssetDao;
import com.taoding.mapper.fixedAsset.IntangibleAssetDao;
import com.taoding.mapper.fixedAsset.LongApportionedAssetsDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.vouchersummary.CpaVoucherSummaryService;

/**
 * 
 * @ClassName: fixedAssetTool
 * @Description: TODO(资产计算应用类)
 * @author lixc
 * @date 2017年12月1日 下午3:44:12
 *
 */
@Service
@Transactional(readOnly = false)
public class FixedAssetToolServiceImp  implements  FixedAssetToolService{

	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private FixedAssetService fixedAssetService;
	@Autowired
	private LongApportionedAssetsDao longApportionedAssetsDao;
	@Autowired
	private FixedAssetDao fixedAssetDao;
	@Autowired
	private IntangibleAssetDao intangibleAssetDao;
	@Autowired
	private CpaVoucherSummaryService voucherSummaryService ;
	@Autowired
	private CpaCustomerSubjectService customerSubjectService;

	// 求和资源原值
	public BigDecimal originalVlaueSum(List< ? extends  FixedAsset> list) {
		if (CollectionUtils.isEmpty(list))
			return null; 
		return list.stream().filter(asset -> null != asset.getOriginalValue())
				.map((asset) -> asset.getOriginalValue())
				.reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}

	// 
	/**
	 * 
	* @Description: TODO(资产累计折旧额   初期累计折旧额 + (本月折旧折旧)) 
	* @param list
	* @param type 1 固定 2 无形
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月7日
	 */
	public BigDecimal depreciationPerMonthSum(List< ? extends  FixedAsset> list,int type) {

		if (CollectionUtils.isEmpty(list))
			return null;

		BigDecimal accumulatedSum =BigDecimal.ZERO;
			
		BigDecimal temp =null;
		
		for (FixedAsset fixedAsset : list) {
			temp=fixedAsset.getOriginalDepreciation();
			if(temp!=null ){
				if(null != fixedAsset.getThisMonthDepreciation())
				{
					temp=temp.add(fixedAsset.getThisMonthDepreciation());
				}
				
				accumulatedSum=accumulatedSum.add(temp);
			}
		}
		return accumulatedSum;
		}

	
	/**
	 * 
	* @Description: TODO(工作账薄ID获得客户Id) 
	* @param accountId
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getCustomerIdByAccountId(String accountId){
		
		if(StringUtils.isBlank(accountId)) 
			throw new LogicException("账薄ID不能为空！");
		
		AccountingBook accountingBook = accountingBookService.get(accountId);
		if (null == accountingBook)
			throw new LogicException("账薄不存在！");
		return accountingBook.getCustomerInfoId();
	}
	
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
	public BigDecimal fixedAsset(String accountId,String subNo) throws LogicException {

		if(StringUtils.isBlank(accountId) || StringUtils.isBlank(subNo) ) return null;
		
		CpaVoucherSummary voucherSummary = 
				voucherSummaryService.findInfoAndAmountById(getFixedAssetSubjectId(accountId,subNo),accountId, CpaVoucherSummary.CURRENT_FRONT_DATA);
		if (null == voucherSummary)
			throw new LogicException("账薄所属客户不存在！");
		
		return voucherSummary.getBalance();
	}

	/**
	 * 
	* @Description: TODO(科目累计折旧额) 
	* @param list
	* @param accountId 账薄ID
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	
	public BigDecimal accumulatedDepreciation(List< ? extends  FixedAsset> list,
			String accountId) {

		if (CollectionUtils.isEmpty(list) || StringUtils.isBlank(accountId))
			return null;

		 BigDecimal accumulatedSum = BigDecimal.ZERO;
			
		for (FixedAsset fixedAsset : list) {
			if(null != fixedAsset.getDepreciationCumulationSubject() && StringUtils.isNotBlank(fixedAsset.getDepreciationCumulationSubject().getId())){
				
			CpaVoucherSummary voucherSummary = 
					voucherSummaryService.findInfoAndAmountById(fixedAsset.getDepreciationCumulationSubject().getId(),accountId, CpaVoucherSummary.CURRENT_FRONT_DATA);
			if(null != voucherSummary && null != voucherSummary.getBalance())
				 accumulatedSum = accumulatedSum.add(voucherSummary.getBalance());
			}
		}
		return accumulatedSum;
	}
	
	/**
	 * 
	* @Description: TODO(计算科目累计折旧额) 
	* @param fixedAsset
	* @param period  计算账期
	* @param type 1 固定 2 无形
	* @return BigDecimal 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月5日
	 */
	public BigDecimal getAccumulatedDepreciationByFixedAsset(FixedAsset fixedAsset,Date period,int type){
		
		if (null == fixedAsset || null == fixedAsset.getDepreciationType())
			return null;

		BigDecimal dTemp = null;
		// 平均年限法
		if (FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR == fixedAsset
				.getDepreciationType()) {
			dTemp=fixedAsset.getDepreciationPerMonth();
			
		    int Mcount = DateUtils.getMonthsBetween(period, fixedAsset.getDepreciationStartDate());
			
			if(null != dTemp && Mcount>0)
				dTemp=dTemp.multiply(new BigDecimal(Mcount)).setScale(Global.KEEP_DIGITS);
			
		
		} else if (FixedAsset.DEPRECIATION_TYPE_DOUBLE_BALANCE_DECLINE == fixedAsset
				.getDepreciationType()) {
			// 双倍余客递减
			  dTemp = MathUtils.sumDepreciationTypeDouble(fixedAsset.getResidualRate(),
					fixedAsset.getDepreciationStartDate(), 
					fixedAsset.getOriginalDepreciation(), 
					fixedAsset.getLifecycle(), 
					fixedAsset.getOriginalValue(),
					period, Global.KEEP_DIGITS,type);
			
		} else if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == fixedAsset
				.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == fixedAsset
						.getDepreciationType()) {
			// 一次提足折旧;一产欠摊销
			  dTemp = MathUtils.getDepreciationTypeOne(
					fixedAsset.getResidualRate(),
					fixedAsset.getOriginalValue(), Global.KEEP_DIGITS,type);
			  
			   int Mcount = DateUtils.getMonthsBetween(period, fixedAsset.getDepreciationStartDate());
				
				if(null != dTemp && Mcount>0)
					dTemp=dTemp.multiply(new BigDecimal(Mcount)).setScale(Global.KEEP_DIGITS);
			  

		}else if (FixedAsset.DEPRECIATION_TYPE_NO_AMORTIZATION == fixedAsset.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_NO_DEPRECIATION == fixedAsset.getDepreciationType()) {
		}
		return dTemp;
	}
	
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
			CpaVoucher voucher,Date period,int type) {
		if (null == fixedAsset || null == voucher)
			return;

		if (CollectionUtils.isEmpty(voucher.getSubjectLists()))
			voucher.setSubjectLists(Lists.newArrayList());
		
		String strType = "";
		String assetNo = "";
		if(AssetType.FIXED_ASSET_TYPE==type){
			strType="固定资产清理";
			assetNo = Global.FIXED_ASSET_NO;
		}else if(AssetType.INTANGIBLE_ASSET_TYPE==type){
			strType="无形资产处理";
			assetNo = Global.INTANGIBLE_ASSET_NO;
		}else if(AssetType.APPORTIONED_ASSET_TYPE==type){
			strType="待摊资产处理";
			assetNo = Global.INTANGIBLE_ASSET_NO;
		}
		
		
		// 固定资产
		// 原值 - 累计折旧额
		List<FixedAsset> list = Lists.newArrayList();
		list.add(fixedAsset);
		BigDecimal AccumulatedDepreciationAmount = depreciationPerMonthSum(list, type);

		
		CpaVoucherSubject voucherSubject = new CpaVoucherSubject();
		
		voucherSubject.setAbstracts(fixedAsset.getName() + strType);
		
		if (null != fixedAsset.getOriginalValue()){
			voucherSubject.setAmountCredit(fixedAsset.getOriginalValue().subtract(AccumulatedDepreciationAmount));
		}
		voucherSubject.setAmountDebit(BigDecimal.ZERO);
		
		voucherSubject.setSubjectId(getFixedAssetSubjectId(fixedAsset.getAccountId(),assetNo));
		voucher.getSubjectLists().add(voucherSubject);
		
		
		voucherSubject = new CpaVoucherSubject();
		voucherSubject.setAbstracts(fixedAsset.getName() + strType);
		// 固定资产清理
		if (null != fixedAsset.getOriginalValue()) 
			voucherSubject.setAmountDebit(fixedAsset.getOriginalValue().subtract(AccumulatedDepreciationAmount));
		    voucherSubject.setAmountCredit(BigDecimal.ZERO);
		
		voucherSubject.setSubjectId(getFixedAssetClearSubjectId(fixedAsset.getAccountId()));
		voucher.getSubjectLists().add(voucherSubject);
	}

	/**
	 * 
	* @Description: TODO(获得固定资产 科目ID) 
	* @param bookId 账簿ID
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getFixedAssetSubjectId(String bookId,String code){
		
		if(null == bookId || StringUtils.isBlank(code)) return null;
		
			 CpaCustomerSubject customerSubject = customerSubjectService
					.findBySubjectNo(bookId,code);

			 
			if (null == customerSubject && Global.FIXED_ASSET_NO.equals(code))
			{
				throw new LogicException("固定资产科目不存在！");
			}else if (null == customerSubject && Global.INTANGIBLE_ASSET_NO.equals(code)){
				
				throw new LogicException("无形资产科目不存在！");
			}
				

		return customerSubject.getId();
	}
	/**
	 * 
	* @Description: TODO(获得清理固定资产 科目ID) 
	* @param bookId 账簿Id
	* @return String 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月2日
	 */
	public String  getFixedAssetClearSubjectId(String bookId){
			
		if(null == bookId) return null;
		
		 CpaCustomerSubject customerSubject = customerSubjectService
				.findBySubjectNo(bookId,
						Global.FIXED_ASSET_CLEAR_NO);

		if (null == customerSubject)
			throw new LogicException("固定资清理产科目不存在！");
	   return customerSubject.getId();
		}
	
	@Override
	public void validataAssetBean(FixedAsset entity, int type) {
		
		if(null == entity) 
			throw new LogicException("资产不能为空！");
		
		String StrType=null;
		if(AssetType.FIXED_ASSET_TYPE == type){
			StrType ="折旧";
			if(null ==entity.getResidualRate()){
				throw new LogicException("残值率不能为空!");	
			}
		}else{
			StrType ="摊销";
		}
		
		if(null ==entity.getOriginalDepreciation()){
			throw new LogicException("期初累计"+StrType+"额值!");	
		}
		
		if(null ==entity.getOriginalDepreciation()){
			throw new LogicException("期初累计"+StrType+"额值!");	
		}
		
		if(null ==entity.getDepreciationStartDate()){
			throw new LogicException("起始"+StrType+"日期不能为空!");	
		}
		
	}
	
	/**
	 * 
	* 净值=原值-期初累计摊销额值-月摊销额
	* @param originalValue  原值
	* @param originalDepreciation 期初累计摊销额值
	* @param depreciationPerMonth  月摊销额
	* @author mhb
	* @date 2017年12月12日
	 */
	public BigDecimal netWorth(BigDecimal originalValue,BigDecimal originalDepreciation,BigDecimal depreciationPerMonth){
		BigDecimal netWorth=new BigDecimal("0.00");
		if(originalValue==null||originalDepreciation==null||depreciationPerMonth==null){
			return netWorth;
		}
		netWorth=originalValue.subtract(originalDepreciation.add(depreciationPerMonth)).setScale(Global.KEEP_DIGITS,BigDecimal.ROUND_HALF_DOWN);
		return netWorth;
	}
	@Transactional
	public String nextCode(String bookId,Integer assetType){
		String codeStr="";
		if(assetType.equals(AssetType.FIXED_ASSET_TYPE)){   //固定资产
			codeStr = fixedAssetDao.findBookMaxCode(bookId);
		}else if(assetType.equals(AssetType.INTANGIBLE_ASSET_TYPE)){  //无形资产
			codeStr = intangibleAssetDao.findBookMaxCode(bookId);
		}else if (assetType.equals(AssetType.APPORTIONED_ASSET_TYPE)){  //待摊资产
			 codeStr = longApportionedAssetsDao.findBookMaxCode(bookId);
		}
		if(StringUtils.isEmpty(codeStr)){
			return FixedAsset.DEFAULT_CODE;
		}
		Integer nextCode= Integer.valueOf(codeStr)+1;
		if(nextCode.toString().length()>=FixedAsset.DEFAULT_CODE.length()){
			return nextCode.toString();
		} 
	   return FixedAsset.DEFAULT_CODE.substring(0, FixedAsset.DEFAULT_CODE.length()-nextCode.toString().length())+nextCode.toString().length();
	}
	
	public List<CpaVoucherSubject> getSubjectAndSumthisMonthDepreciation(String bookId,int type,boolean isDebit,String abstractsName){
		
		if(StringUtils.isBlank(bookId)) return null;
		//获得当前账期
		String currentPriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
	     Map<String, Object> queryMap = new HashMap<String, Object>();
	     queryMap.put("accountId", bookId);
	     queryMap.put("currentPeriod", DateUtils.parseDate(currentPriod));
	     queryMap.put("status", FixedAsset.STATUS_NORMAL);
	     //depreciation_fee_subject_id 折旧费用科目 借方
         //depreciation_cumulation_subject_id 累计折旧科目 贷方
	     if(isDebit){
	    	 queryMap.put("groupBy", "depreciation_fee_subject_id");
	    	 queryMap.put("groupByNameStr", "a.depreciation_fee_subject_id as 'depreciationFeeSubject.Id' ,"); 
	     }else{
	    	 queryMap.put("groupBy", "depreciation_cumulation_subject_id"); 
	    	 queryMap.put("groupByNameStr", "a.depreciation_cumulation_subject_id as 'depreciationCumulationSubject.Id' ,"); 
	     }
	     
		if (AssetType.FIXED_ASSET_TYPE == type) {// 统计固定资产当月折旧
			List<FixedAsset> fixedAssetList = fixedAssetDao.sumFixedAssetListByMap(queryMap);
			if(CollectionUtils.isNotEmpty(fixedAssetList)){
			return convertFixedToSubjectList(fixedAssetList,isDebit,abstractsName);
			}
		} else if (AssetType.INTANGIBLE_ASSET_TYPE == type) {// 统计无形资产当月折旧
			List<IntangibleAsset> intangibleAssetList = intangibleAssetDao.sumIntangibleAssetListByMap(queryMap);
			if(CollectionUtils.isNotEmpty(intangibleAssetList)){
				return convertFixedToSubjectList(intangibleAssetList,isDebit,abstractsName);
			}
		} else if (AssetType.APPORTIONED_ASSET_TYPE == type) {// 统计长期摊销当月折旧
			List<LongApportionedAssets> longApportionedAssetsList = longApportionedAssetsDao.sumLongApportionedAssetsListByMap(queryMap);
			if(null != longApportionedAssetsList){
				return convertFixedToSubjectList(longApportionedAssetsList,isDebit,abstractsName);
			}
		}
		
		return null;
	}
	
	private List<CpaVoucherSubject> convertFixedToSubjectList(List<? extends FixedAsset> list,boolean isDebit,String abstractsName){
		
		if(CollectionUtils.isEmpty(list)) return null;
		List<CpaVoucherSubject> returnList = 
						    list.stream().map(asset -> {
								if(null != asset)
								{
									String subjectId = "";
									if(null != asset.getDepreciationCumulationSubject() && StringUtils.isNotBlank(asset.getDepreciationCumulationSubject().getId())){
										subjectId = asset.getDepreciationCumulationSubject().getId();
									}else if(null != asset.getDepreciationFeeSubject() && StringUtils.isNotBlank(asset.getDepreciationFeeSubject().getId())){
										subjectId = asset.getDepreciationFeeSubject().getId();
									}
									
									if(StringUtils.isNotBlank(subjectId)){
										CpaVoucherSubject subject = new CpaVoucherSubject();
										subject.setSubjectId(subjectId);
										subject.setAbstracts(abstractsName);
										if(isDebit){
											subject.setSumDebit(asset.getThisMonthDepreciation());
										}else{
											subject.setSumCredit(asset.getThisMonthDepreciation());
										}
									   return subject;
									}
								}
								return null;
							}).filter(map ->  map != null).collect(Collectors.toList());
							
		return returnList;
	}

	@Override
	public boolean updateStatusByAssetList(List<? extends FixedAsset> list,Integer status,Integer type) {
		
		return false;
	}

	@Override
	public boolean batchInsertAssetList(List<? extends FixedAsset> list,Integer type) {
		
		
		// 插入下月资产 并计算 净值 初期累计 结账日期等   通过 起始折旧日期判断
	    AssetUtils.changeAssetListBeforSave(list);
		return false;
	}
}
