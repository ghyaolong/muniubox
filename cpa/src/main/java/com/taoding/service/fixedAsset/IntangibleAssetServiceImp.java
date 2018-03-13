package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.MathUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.AssetTypeAndAsset;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.IntangibleAsset;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.fixedAsset.IntangibleAssetDao;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.voucher.CpaVoucherService;
/**
 * 
* @ClassName: FixedAssetServiceImp 
* @Description: TODO(固定资产业务层) 
* @author lixc 
* @date 2017年12月1日 下午3:55:22 
*
 */
@Service
@Transactional(readOnly=true)
public class IntangibleAssetServiceImp implements IntangibleAssetService {

	@Autowired
	private CpaCustomerSubjectService customerSubjectService;
	
	@Autowired
	private FixedAssetToolService fixedAssetTool;
	
	@Autowired
	private CpaVoucherService voucherService;
	
	@Autowired 
	private AccountingBookDao accountingBookDao;
	
	@Autowired
	private AssetTypeAndAssetService assetTypeAndAssetService;
	
	@Autowired 
	private IntangibleAssetDao  dao;
	
	@Autowired
	private FixedAssetService fixedAssetService;
	
	@Override
	@Transactional(readOnly=false)
	public void saveIntangibleAsset(IntangibleAsset intangibleAsset) {
		 
		//验证合法性
		fixedAssetTool.validataAssetBean(intangibleAsset ,AssetType.INTANGIBLE_ASSET_TYPE);
		
		//验证摊销方式是否正确
        boolean flag=checkDepreciationType(intangibleAsset,intangibleAsset.getCurrentPeriod());
        if(!flag){
			throw new LogicException("摊销方式验证失败！");
		}
        
        if(StringUtils.isBlank(intangibleAsset.getId())){//添加
        	intangibleAsset.setEnterDate(new Date());
        }
        
        //处理管来查询
        AssetTypeAndAsset assetTypeAndAsset = new AssetTypeAndAsset();
        assetTypeAndAsset.setAccountId(intangibleAsset.getAccountId());
        assetTypeAndAsset.setAssetName(intangibleAsset.getName());
        assetTypeAndAsset.setAssetTypeId(intangibleAsset.getAssetTypeId());
        assetTypeAndAsset.setType(AssetType.INTANGIBLE_ASSET_TYPE);
        assetTypeAndAssetService.checkAssetTypeAndAsset(assetTypeAndAsset);
    	
        AssetUtils.changeSave(intangibleAsset);//处理 净值 本月结账  入账日期
        
        save(intangibleAsset);	
	}

	@Override
	public PageInfo<IntangibleAsset> findIntangibleAssetByPage(
			Map<String, Object> queryMap) {
		if(null != queryMap){
			
			String accountId = queryMap.get("accountId")==null?"":queryMap.get("accountId").toString();
			Date currentPeriod = queryMap.get("currentPeriod")==null?null:DateUtils.parseDate(queryMap.get("currentPeriod").toString());
			
			if(StringUtils.isBlank(accountId)){
				throw new LogicException("账簿不能为空！");
			}
			
			if(currentPeriod==null){
				throw new LogicException("账期不能为空！");
			}
			if(queryMap.get("isAll") == null || "".equals(queryMap.get("isAll")) || "false".equals(queryMap.get("isAll"))){
				PageUtils.page(queryMap);
			}
			List<IntangibleAsset> list = dao.findIntangibleAssetListByMap(queryMap); 
				//是否添加统计功能
				if(queryMap.get("isTotUp") == null || "".equals(queryMap.get("isTotUp")) || "false".equals(queryMap.get("isTotUp"))){
					queryMap.put("groupby", "a.current_period ,a.account_id");
					list.addAll(dao.sumIntangibleAssetListByMap(queryMap));
				}
			 
			  return new PageInfo<IntangibleAsset>(list);
		}
		
		return null;
	}

	@Override
	@Transactional(readOnly=false)
	public int deleteIntangibleAssetByIds(String ids) {
		
		if(StringUtils.isBlank(ids)) return 0;
		
		String [] arrayStr  =ids.split(",");
		if(ArrayUtils.isEmpty(arrayStr)) return 0;
		
		int delCount=0;
		for (int i = 0; i < arrayStr.length; i++) {
			dao.delete(arrayStr[i]);
			delCount++;
		}
		return delCount;
	}
	
	
	@Override
	public boolean checkDepreciationType(IntangibleAsset intangibleAsset,Date period) {
		if (null == intangibleAsset || null == intangibleAsset.getDepreciationType())
			return false;
		
		boolean flag = false;
		// 平均年限法
		if (FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR == intangibleAsset
				.getDepreciationType()) {
			BigDecimal dTemp = MathUtils.getDepreciationTypeYear(null, intangibleAsset.getLifecycle(),
					intangibleAsset.getOriginalValue(), Global.KEEP_DIGITS);
			if (null != intangibleAsset.getDepreciationPerMonth()
					&& intangibleAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				throw new LogicException("平均年限法运算月摊销额【" + dTemp.doubleValue()
						+ "】与【"
						+ intangibleAsset.getDepreciationPerMonth().doubleValue()
						+ "】不一致！");
			}
			flag = true;
		} else if (FixedAsset.DEPRECIATION_TYPE_DOUBLE_BALANCE_DECLINE == intangibleAsset
				.getDepreciationType()) {
			// 双倍余客递减
			BigDecimal dTemp = MathUtils.getDepreciationTypeDouble(null,
					intangibleAsset.getDepreciationStartDate(), 
					intangibleAsset.getOriginalDepreciation(), 
					intangibleAsset.getLifecycle(), 
					intangibleAsset.getOriginalValue(),
					period, Global.KEEP_DIGITS,AssetType.INTANGIBLE_ASSET_TYPE);
			
			if (null != intangibleAsset.getDepreciationPerMonth()
					&& intangibleAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				throw new LogicException("双倍余客递减方法运算月摊销额【"
						+ dTemp.doubleValue() + "】与【"
						+ intangibleAsset.getDepreciationPerMonth().doubleValue()
						+ "】不一致！");
			}
			flag = true;
		} else if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == intangibleAsset
				.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == intangibleAsset
						.getDepreciationType()) {
			// 一次提足折旧;一产欠摊销
			BigDecimal dTemp = MathUtils.getDepreciationTypeOne(
					intangibleAsset.getResidualRate(),
					intangibleAsset.getOriginalValue(), Global.KEEP_DIGITS,AssetType.INTANGIBLE_ASSET_TYPE);

			if (null != intangibleAsset.getDepreciationPerMonth()
					&& intangibleAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == intangibleAsset
						.getDepreciationType()) {
					throw new LogicException("一次提足折旧方法运算月摊销额【"
							+ dTemp.doubleValue()
							+ "】与【"
							+ intangibleAsset.getDepreciationPerMonth()
									.doubleValue() + "】不一致！");
				} else if (FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == intangibleAsset
						.getDepreciationType()) {
					throw new LogicException("一次提足折旧方法运算月叹摊销额【"
							+ dTemp.doubleValue()
							+ "】与【"
							+ intangibleAsset.getDepreciationPerMonth()
									.doubleValue() + "】不一致！");
				}
			}
			flag = true;
		}else if (FixedAsset.DEPRECIATION_TYPE_NO_AMORTIZATION == intangibleAsset
				.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_NO_DEPRECIATION == intangibleAsset
						.getDepreciationType()) {
			flag = true;
		}
		return flag;
	}
	
	/**
	* @Description: TODO(对账) 
	* @param accountId 账簿ID
	* @return Map<String,Object> 
	* {isAssetEquilibria:true,//是否资产平衡
	* isAccumulatedEquilibria:true,//是否资源平衡
	* resourcesOriginal :1231,//资源原值
	* assetsAccumulatedDepreciation:21231,//资产累计折旧
	* IntangibleAssets:1212,//固定资产
	* accumulatedDepreciation:23423 //累计折旧
	* } 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月1日
	 */
	public Map<String, Object> reconciliation(String accountId){
		
        if(StringUtils.isBlank(accountId))  throw new LogicException("账簿不能为空！");
       
        //获得当期那账期
		String StrCurrentPeriod =CurrentAccountingUtils.getCurrentVoucherPeriod(accountId);//获得档期账期
		
        Map<String, Object>  queryMap = Maps.newHashMap();
        queryMap.put("accountId", accountId);
        queryMap.put("currentPeriod", DateUtils.parseDate(StrCurrentPeriod));
        
		List<IntangibleAsset> list=dao.findSingleIntangibleAssetListByMap(queryMap);
		
		if(CollectionUtils.isEmpty(list)) return null;
		
		//资产原值
		BigDecimal originalVlaueSum =fixedAssetTool.originalVlaueSum(list);
 		
		//资产累计摊销额   初期累计摊销额 + (本月摊销)
		BigDecimal depreciationPerMonthSum = fixedAssetTool.depreciationPerMonthSum(list,AssetType.INTANGIBLE_ASSET_TYPE);
		
		//科目无形资产 编号 1701
		BigDecimal fixedAsset = fixedAssetTool.fixedAsset(accountId,Global.INTANGIBLE_ASSET_NO);
		
	    //科目累计折旧额
		BigDecimal accumulatedDepreciation = fixedAssetTool.accumulatedDepreciation(list,accountId);
		
		Map<String, Object> resultMap = Maps.newHashMap();
		//求和资源原值
		if(null != originalVlaueSum){
			resultMap.put("resourcesOriginal", originalVlaueSum.setScale(Global.KEEP_DIGITS).doubleValue());
		}
		
		//资产累计摊销额
		if(null != depreciationPerMonthSum){
			resultMap.put("assetsAccumulatedDepreciation", depreciationPerMonthSum.setScale(Global.KEEP_DIGITS).doubleValue());
		}
		
		//固定资产
		if(null != fixedAsset){
			resultMap.put("IntangibleAssets", fixedAsset.setScale(Global.KEEP_DIGITS).doubleValue());
		}
		
		//资产累计折旧额
		if(null != accumulatedDepreciation){
			resultMap.put("accumulatedDepreciation", accumulatedDepreciation.setScale(Global.KEEP_DIGITS).doubleValue());
		}
		
		//资产平衡
		if(originalVlaueSum.compareTo(fixedAsset)==0){
			resultMap.put("isAssetEquilibria", true);
		}else{
			resultMap.put("isAssetEquilibria", false);
		}
		//累计平衡
		if(depreciationPerMonthSum.compareTo(accumulatedDepreciation)==0){
			resultMap.put("isAccumulatedEquilibria", true);
		}else{
			resultMap.put("isAccumulatedEquilibria", false);
		}
		return resultMap;
	}
	
	
	@Override
	@Transactional(readOnly=false)
	public int batctHandedByIds(String ids){
		
		if(StringUtils.isBlank(ids)) return 0;
		
		String [] arrayIds = ids.split(",");
		if(ArrayUtils.isEmpty(arrayIds)) return 0;
		
		IntangibleAsset intangibleAsset = null;
	    int count =0;
	    
		for (String id : arrayIds) {
			
			intangibleAsset = dao.get(id);
			 if(null == intangibleAsset) throw new LogicException("参数【"+id+"】不存在无形资产！");
			 count++; 
			 HandedBy(intangibleAsset, AssetType.INTANGIBLE_ASSET_TYPE);
		}
          
		return count;
	}
	
	@Transactional(readOnly = false)
	public void save(IntangibleAsset intangibleAsset){
			if (intangibleAsset.getIsNewRecord()){
				intangibleAsset.setId(UUIDUtils.getUUid());
				fixedAssetService.preInsert(intangibleAsset);
				fixedAssetService.preUpdate(intangibleAsset);
				intangibleAsset.setCode(fixedAssetTool.nextCode(intangibleAsset.getAccountId(), AssetType.APPORTIONED_ASSET_TYPE));
				dao.insert(intangibleAsset);
			}else{
				fixedAssetService.preUpdate(intangibleAsset);
				dao.update(intangibleAsset);
			}
		}
	
	
	public IntangibleAsset get(String id){
		return dao.get(id);
	}
	//企业会计准则默认6602.018管理费用-无形资产摊销，小企业会计准则默认5602.018 累计摊销
	//累计摊销科目（1702累计摊销）
	// {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	@Override
	public Map<String, Object> getFeeSubjectAndCumulationSubjectByBookId(String bookId){
		
		AccountingBook accountingBook =	accountingBookDao.get(bookId);
		
		if(null != accountingBook && StringUtils.isNotBlank(accountingBook.getAccountingSystemId())){
			Map<String, Object>  resultMap =  Maps.newHashMap(); 
			
			 if(AccountSystem.SMALL_ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
				
				 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "5602.018");
				 resultMap.put("depreciationFeeSubject", customerSubject);
			 }else if(AccountSystem.ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
				 
				 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "6602.018");
				 resultMap.put("depreciationFeeSubject", customerSubject);
				 
			 }
			
			 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "1702");
			 resultMap.put("depreciationCumulationSubject", customerSubject);
			 
			 return resultMap;
		}
		
		return null;
	}
	
	
	/**
	 * 
	* @Description: TODO(获得月摊销额) 
	* @param originalValue
	* @param originalDepreciation
	* @param lifecycle
	* @param depreciationType
	* @param period
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
			Integer depreciationType){
		
		String periodStr = CurrentAccountingUtils.getCurrentVoucherPeriod(accountBookId);
 		 Date period = DateUtils.parseDate(periodStr);
 		
	return fixedAssetService.getDepreciationPerMonth(null, originalValue, originalDepreciation, lifecycle, depreciationType, period, null, AssetType.INTANGIBLE_ASSET_TYPE);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean  HandedBy(IntangibleAsset fixedAsset,int type){
		 
         String customerId = fixedAssetTool.getCustomerIdByAccountId(fixedAsset.getAccountId());
        
        if(StringUtils.isNotBlank(customerId)) {
		 
        	 CpaVoucher voucher = new CpaVoucher(); 
        	  voucher.setCustomerId(customerId);
	    	  voucher.setVoucherType(2);//资产凭证
	    	  voucher.setVoucherDate(new Date());
	    	  voucher.setBookId(fixedAsset.getAccountId());
	    	 
    		String periodStr = CurrentAccountingUtils.getCurrentVoucherPeriod(fixedAsset.getAccountId());
        	Date period = DateUtils.parseDate(periodStr);
        	fixedAssetTool.getVoucherSubjectByFixedAsset(fixedAsset,voucher, period,type);
	    	  
	         //插入凭证
	    	  @SuppressWarnings("unchecked")
			 Map<String , Object> voucherMap = (Map<String , Object>)voucherService.insertCpaVoucher(voucher);
	    	  
	    	 if(null == voucherMap || !Boolean.parseBoolean(String.valueOf(voucherMap.get("state")))){
	    		 throw new LogicException("插入凭证失败！"); 
	    	 }else{
	    		 fixedAsset.setStatus(FixedAsset.STATUS_ALREADY_CLEAN_OR_HANDE);
	    		 fixedAsset.setVoucherId(String.valueOf(voucherMap.get("id")));
	    		 dao.update(fixedAsset);
	    		 return true;
	    	 }
	      }
      
    return false;		
	}
}

