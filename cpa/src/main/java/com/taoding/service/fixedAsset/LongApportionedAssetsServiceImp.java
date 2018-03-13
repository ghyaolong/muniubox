package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.fixedAsset.LongApportionedAssetsDao;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.voucher.CpaVoucherService;

@Service
@Transactional
public class LongApportionedAssetsServiceImp implements
		LongApportionedAssetsService {

	@Autowired
	private FixedAssetToolService fixedAssetTool;
	@Autowired
	private LongApportionedAssetsDao dao;

	@Autowired
	private FixedAssetService fixedAssetService;
	@Autowired 
	private AccountingBookDao accountingBookDao;
	
	@Autowired
	private CpaCustomerSubjectService customerSubjectService;
	
	@Autowired
	private CpaVoucherService voucherService;

	@Override
	@Transactional(readOnly = false)
	public void saveApportionedAssets(LongApportionedAssets longApportionedAssets) {
		// 验证合法性
		fixedAssetTool.validataAssetBean(longApportionedAssets,AssetType.APPORTIONED_ASSET_TYPE);
		// 验证待摊方式是否正确
		boolean flag = checkDepreciationType(longApportionedAssets,longApportionedAssets.getCurrentPeriod());
		if (!flag) {
			throw new LogicException("待摊方式验证失败！");
		}
		//本月摊销赋值   月摊销额=本月摊销额
		if(longApportionedAssets.getDepreciationPerMonth()!=null){
			longApportionedAssets.setThisMonthDepreciation(longApportionedAssets.getDepreciationPerMonth());
		}
		longApportionedAssets.setNetWorth(fixedAssetTool.netWorth(longApportionedAssets.getOriginalValue(), longApportionedAssets.getOriginalDepreciation(), longApportionedAssets.getDepreciationPerMonth()));
		save(longApportionedAssets);
	}

	@Transactional(readOnly = false)
	public void save(LongApportionedAssets longApportionedAssets) {
		if (longApportionedAssets.getIsNewRecord()) {
			longApportionedAssets.setId(UUIDUtils.getUUid());
			fixedAssetService.preInsert(longApportionedAssets);
			fixedAssetService.preUpdate(longApportionedAssets);
			longApportionedAssets.setCode(fixedAssetTool.nextCode(longApportionedAssets.getAccountId(), AssetType.APPORTIONED_ASSET_TYPE));
			dao.insert(longApportionedAssets);
		} else {
			fixedAssetService.preUpdate(longApportionedAssets);
			dao.update(longApportionedAssets);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void batchDeleteApportionedAssets(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new LogicException("选择批量信息为空!");
		}
		String[] strIds = ids.split(",");
		for (String id : strIds) {
		LongApportionedAssets longApportionedAssets=dao.get(id);
		   if(longApportionedAssets==null||longApportionedAssets.getStatus().equals(FixedAsset.STATUS_CLOSE_ACCOUNTS)){
			   throw new LogicException("固定资产已结账的不能删除");
		   }
		}
		dao.batchUpdate(strIds);

	}
 
	@Transactional(readOnly = false) 
	public boolean checkDepreciationType(LongApportionedAssets longApportionedAssets,Date period) {
		if (null == longApportionedAssets || null == longApportionedAssets.getDepreciationType())
			return false;
		
		boolean flag = false;
		// 平均年限法
		if (FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR == longApportionedAssets
				.getDepreciationType()) {
			BigDecimal dTemp = MathUtils.getDepreciationTypeYear(null, longApportionedAssets.getLifecycle(),
					longApportionedAssets.getOriginalValue(), Global.KEEP_DIGITS);
			if (null != longApportionedAssets.getDepreciationPerMonth()
					&& longApportionedAssets.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				throw new LogicException("平均年限法运算月摊销额【" + dTemp.doubleValue()+ "】与【"+ longApportionedAssets.getDepreciationPerMonth().doubleValue()+ "】不一致！");
			}
			 flag = true;
		}else if(FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == longApportionedAssets.getDepreciationType()){
			if(longApportionedAssets.getOriginalValue().compareTo(longApportionedAssets.getDepreciationPerMonth())!=0){
				throw new LogicException("一次摊销方法原值和月摊销值【" + longApportionedAssets.getOriginalValue().doubleValue()+ "】与【"+ longApportionedAssets.getDepreciationPerMonth().doubleValue()+ "】不一致！");
			}
			  flag = true;
		} else if( FixedAsset.DEPRECIATION_TYPE_NO_AMORTIZATION == longApportionedAssets.getDepreciationType()){
				flag = true;
		}
		return flag;
	}

	@Override
	@Transactional(readOnly = false)
	public PageInfo<LongApportionedAssets> findListByPage(Map<String, Object> queryMap) {
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
			List<LongApportionedAssets>  list = dao.findListByPage(queryMap); 
				//是否添加统计功能
			if(list.size()>0){
				if(queryMap.get("isTotUp") == null || "".equals(queryMap.get("isTotUp")) || "false".equals(queryMap.get("isTotUp"))){
					queryMap.put("groupby", "a.current_period ,a.account_id");
					list.addAll(dao.sumLongApportionedAssetsListByMap(queryMap)); 
				}
			}
			  return new PageInfo<LongApportionedAssets>(list);
		}
		
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public Object reconciliation(String accountId) {
		 if(StringUtils.isBlank(accountId))  throw new LogicException("账簿不能为空！");
		 String StrCurrentPeriod =CurrentAccountingUtils.getCurrentVoucherPeriod(accountId);//获得档期账期
		 Map<String, Object>  queryMap = Maps.newHashMap();
	        queryMap.put("accountId", accountId);
	        queryMap.put("currentPeriod", DateUtils.parseDate(StrCurrentPeriod));
	        LongApportionedAssets longApportionedAssets = null;
	        List<LongApportionedAssets> longList = dao.sumLongApportionedAssetsListByMap(queryMap);
			if(CollectionUtils.isNotEmpty(longList)){
				longApportionedAssets=longList.get(0);
			}
					
	        Map<String, Object> resultMap = Maps.newHashMap();
		    if(longApportionedAssets!=null){
		    	if(longApportionedAssets.getNetWorth()!=null){
			    	BigDecimal sumNetWorth= longApportionedAssets.getNetWorth();
			    	//净值
			    	if(null != sumNetWorth){
						resultMap.put("sumNetWorth", sumNetWorth.setScale(Global.KEEP_DIGITS,BigDecimal.ROUND_HALF_DOWN).doubleValue());
					}
			    	//获取长期待摊费用 1801
					BigDecimal  startLongApprtionedAssetsCost = fixedAssetTool.fixedAsset(accountId,Global.LONG_APPORTIONED_ASSET_NO);
					if(null != startLongApprtionedAssetsCost){
						resultMap.put("longApprtionedAssetsCost", startLongApprtionedAssetsCost.setScale(Global.KEEP_DIGITS,BigDecimal.ROUND_HALF_DOWN).doubleValue());
					}
					if(sumNetWorth.compareTo(startLongApprtionedAssetsCost)==0){
						resultMap.put("isAssetEquilibria", true);
					}else{
						resultMap.put("isAssetEquilibria", false);
					}
		    	}
		    }
		return resultMap;
	}
	//摊销费用科目（企业会计准则默认6602.019管理费用-长期待摊费用摊销，小企业会计准则默认5602.019管理费用-长期待摊销费用）可切换），
	//累计摊销科目（企业会计准侧默认1801长期待摊费用；小企业会计准则默认1801长期待摊费用，可切换）， 
		@Override
		public Map<String, Object> getFeeSubjectAndCumulationSubjectByBookId(String bookId){
			
			AccountingBook accountingBook =	accountingBookDao.get(bookId);
			
			if(null != accountingBook && StringUtils.isNotBlank(accountingBook.getAccountingSystemId())){
				Map<String, Object>  resultMap =  Maps.newHashMap(); 
				
				 if(AccountSystem.SMALL_ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
					
					 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "5602.019");
					 resultMap.put("depreciationFeeSubject", customerSubject);
				 }else if(AccountSystem.ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
					 
					 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "6602.019");
					 resultMap.put("depreciationFeeSubject", customerSubject);
					 
				 }
				
				 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "1801");
				 resultMap.put("depreciationCumulationSubject", customerSubject);
				 
				 return resultMap;
			}
			
			return null;
		}

		@Override
		@Transactional(readOnly=false)
		public void batctHandedByIds(String ids){
			if(StringUtils.isBlank(ids)) {
				 throw new LogicException("数据异常！");
			}
			
			String [] arrayIds = ids.split(","); 
			LongApportionedAssets longApportionedAssets  =  null;
			for (String id : arrayIds) {
				longApportionedAssets = dao.get(id);
			     if(null == longApportionedAssets) {
			    	 throw new LogicException("参数【"+id+"】不存在固定资产！");
			     }
				if(!HandedBy(longApportionedAssets, AssetType.APPORTIONED_ASSET_TYPE)){
					throw new LogicException("处置失败");
				}
			}
		}
		
		@Transactional(readOnly = false)
		public boolean  HandedBy(LongApportionedAssets fixedAsset,int type){
			 
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

		@Override 
		@Transactional(readOnly = false)
		public LongApportionedAssets get(String id){
				return dao.get(id);
    	}

		@Override
		@Transactional(readOnly = false)
		public Object getDepreciationPerMonth(BigDecimal originalValue,Integer lifecycle, Integer depreciationType) { 
			Map<String, Object> map= new HashMap<String, Object>();
			
			//月摊销额
	 		BigDecimal depreciationPerMonth = MathUtils.getDepreciationTypeYear(null, lifecycle,originalValue, Global.KEEP_DIGITS);
	 		map.put("depreciationPerMonth", depreciationPerMonth);
	 		//期初累计摊销额
	 		/*map.put("originalDepreciation", originalDepreciation)*/
	 		//
			return map;
		}
}
