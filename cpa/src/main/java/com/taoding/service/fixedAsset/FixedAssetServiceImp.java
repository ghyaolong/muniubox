package com.taoding.service.fixedAsset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.MathUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.fixedAsset.AssetType;
import com.taoding.domain.fixedAsset.AssetTypeAndAsset;
import com.taoding.domain.fixedAsset.DepreciationDetail;
import com.taoding.domain.fixedAsset.FixedAsset;
import com.taoding.domain.fixedAsset.IntangibleAsset;
import com.taoding.domain.fixedAsset.LongApportionedAssets;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.mapper.fixedAsset.AssetTypeDao;
import com.taoding.mapper.fixedAsset.FixedAssetDao;
import com.taoding.mapper.fixedAsset.IntangibleAssetDao;
import com.taoding.mapper.fixedAsset.LongApportionedAssetsDao;
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
@Slf4j
@Service
@Transactional(readOnly = true)
public class FixedAssetServiceImp extends
		DefaultCurdServiceImpl<FixedAssetDao, FixedAsset> implements
		FixedAssetService {

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
	private IntangibleAssetDao intangibleAssetDao;

	@Autowired
	private LongApportionedAssetsDao longApportionedAssetsDao;

	@Autowired
	private AssetTypeDao assetTypeDao; 
	
	@Autowired
	private AssetTypeService assetTypeService;

	@Override
	@Transactional(readOnly = false)
	public void saveFixedAsset(FixedAsset fixedAsset) {

		// 验证合法性
		fixedAssetTool.validataAssetBean(fixedAsset, AssetType.FIXED_ASSET_TYPE);

		// 验证折旧方式是否正确
		boolean flag = checkDepreciationType(fixedAsset,
				fixedAsset.getCurrentPeriod());
		if (!flag) {
			throw new LogicException("折旧方式验证失败！");
		}

		if (StringUtils.isBlank(fixedAsset.getId())) {// 添加
			fixedAsset.setEnterDate(new Date());
		}
		// 处理管来查询
		AssetTypeAndAsset assetTypeAndAsset = new AssetTypeAndAsset();
		assetTypeAndAsset.setAccountId(fixedAsset.getAccountId());
		assetTypeAndAsset.setAssetName(fixedAsset.getName());
		assetTypeAndAsset.setAssetTypeId(fixedAsset.getAssetTypeId());
		assetTypeAndAsset.setType(AssetType.FIXED_ASSET_TYPE);
		assetTypeAndAssetService.checkAssetTypeAndAsset(assetTypeAndAsset);
		if (fixedAsset.getIsNewRecord()) {
			fixedAsset.setCode(fixedAssetTool.nextCode(fixedAsset.getAccountId(),
							AssetType.APPORTIONED_ASSET_TYPE));
		}
		AssetUtils.changeSave(fixedAsset);//处理 净值 本月结账  入账日期
		save(fixedAsset);
	}

	@Override
	public PageInfo<FixedAsset> findFixedAssetListByPage(
			Map<String, Object> queryMap) {
		if (null != queryMap) {

			String accountId = queryMap.get("accountId") == null ? ""
					: queryMap.get("accountId").toString();
			Date currentPeriod = queryMap.get("currentPeriod") == null ? null
					: DateUtils.parseDate(queryMap.get("currentPeriod")
							.toString());

			if (StringUtils.isBlank(accountId)) {
				throw new LogicException("账簿不能为空！");
			}

			if (currentPeriod == null) {
				throw new LogicException("账期不能为空！");
			}

			if (queryMap.get("isAll") == null
					|| "".equals(queryMap.get("isAll"))
					|| "false".equals(queryMap.get("isAll"))) {
				PageUtils.page(queryMap);
			}

			List<FixedAsset> list = dao.findFixedAssetListByMap(queryMap);
			// 是否添加统计功能
			if (queryMap.get("isTotUp") == null
					|| "".equals(queryMap.get("isTotUp"))
					|| "false".equals(queryMap.get("isTotUp"))) {
				queryMap.put("groupby", "a.current_period ,a.account_id");
				list.addAll(dao.sumFixedAssetListByMap(queryMap));
			}
			return new PageInfo<FixedAsset>(list);
		}

		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public int deleteFixedAssetByIds(String ids) {

		if (StringUtils.isBlank(ids))
			return 0;

		String[] arrayStr = ids.split(",");
		if (ArrayUtils.isEmpty(arrayStr))
			return 0;

		int delCount = 0;
		for (int i = 0; i < arrayStr.length; i++) {
			dao.delete(arrayStr[i]);
			delCount++;
		}
		return delCount;
	}

	@Override
	public boolean checkDepreciationType(FixedAsset fixedAsset, Date period) {
		if (null == fixedAsset || null == fixedAsset.getDepreciationType())
			return false;

		boolean flag = false;
		// 平均年限法
		if (FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR == fixedAsset
				.getDepreciationType()) {
			BigDecimal dTemp = MathUtils.getDepreciationTypeYear(
					fixedAsset.getResidualRate(), fixedAsset.getLifecycle(),
					fixedAsset.getOriginalValue(), Global.KEEP_DIGITS);
			if (null != fixedAsset.getDepreciationPerMonth()
					&& fixedAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				throw new LogicException("平均年限法运算月折旧额【" + dTemp.doubleValue()
						+ "】与【"
						+ fixedAsset.getDepreciationPerMonth().doubleValue()
						+ "】不一致！");
			}
			flag = true;
		} else if (FixedAsset.DEPRECIATION_TYPE_DOUBLE_BALANCE_DECLINE == fixedAsset
				.getDepreciationType()) {
			// 双倍余客递减
			BigDecimal dTemp = MathUtils.getDepreciationTypeDouble(
					fixedAsset.getResidualRate(),
					fixedAsset.getDepreciationStartDate(),
					fixedAsset.getOriginalDepreciation(),
					fixedAsset.getLifecycle(), fixedAsset.getOriginalValue(),
					period, Global.KEEP_DIGITS, AssetType.FIXED_ASSET_TYPE);

			if (null != fixedAsset.getDepreciationPerMonth()
					&& fixedAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				throw new LogicException("双倍余客递减方法运算月折旧额【"
						+ dTemp.doubleValue() + "】与【"
						+ fixedAsset.getDepreciationPerMonth().doubleValue()
						+ "】不一致！");
			}
			flag = true;
		} else if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == fixedAsset
				.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == fixedAsset
						.getDepreciationType()) {
			// 一次提足折旧;一产欠摊销
			BigDecimal dTemp = MathUtils.getDepreciationTypeOne(
					fixedAsset.getResidualRate(),
					fixedAsset.getOriginalValue(), Global.KEEP_DIGITS,
					AssetType.FIXED_ASSET_TYPE);

			if (null != fixedAsset.getDepreciationPerMonth()
					&& fixedAsset.getDepreciationPerMonth().compareTo(dTemp) != 0) {
				if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == fixedAsset
						.getDepreciationType()) {
					throw new LogicException("一次提足折旧方法运算月折旧额【"
							+ dTemp.doubleValue()
							+ "】与【"
							+ fixedAsset.getDepreciationPerMonth()
									.doubleValue() + "】不一致！");
				} else if (FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == fixedAsset
						.getDepreciationType()) {
					throw new LogicException("一次提足折旧方法运算月折旧额【"
							+ dTemp.doubleValue()
							+ "】与【"
							+ fixedAsset.getDepreciationPerMonth()
									.doubleValue() + "】不一致！");
				}
			}
			flag = true;
		} else if (FixedAsset.DEPRECIATION_TYPE_NO_AMORTIZATION == fixedAsset
				.getDepreciationType()
				|| FixedAsset.DEPRECIATION_TYPE_NO_DEPRECIATION == fixedAsset
						.getDepreciationType()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * @Description: TODO(对账)
	 * @param accountId
	 *            账簿ID
	 * @return Map<String,Object> {isAssetEquilibria:true,//是否资产平衡
	 *         isAccumulatedEquilibria:true,//是否资源平衡 resourcesOriginal
	 *         :1231,//资源原值 assetsAccumulatedDepreciation:21231,//资产累计折旧
	 *         fixedAssets:1212,//固定资产 accumulatedDepreciation:23423 //累计折旧 }
	 *         返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月1日
	 */
	public Map<String, Object> reconciliation(String accountId) {
		if (StringUtils.isBlank(accountId))
			return null;

		// 获得当前账期
		// String customerId =
		// fixedAssetTool.getCustomerIdByAccountId(accountId);
		String StrCurrentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(accountId);// 获得档期账期

		Map<String, Object> queryMap = Maps.newHashMap();
		queryMap.put("accountId", accountId);
		queryMap.put("currentPeriod", DateUtils.parseDate(StrCurrentPeriod));
		queryMap.put("orderStr", "a.create_date");

		List<FixedAsset> list = dao.findSingleFixedAssetListByMap(queryMap);

		if (CollectionUtils.isEmpty(list))
			return null;

		// 求和资源原值
		BigDecimal originalVlaueSum = fixedAssetTool.originalVlaueSum(list);

		// 资产累计折旧额 初期累计折旧额 + (本月折旧)
		BigDecimal depreciationPerMonthSum = fixedAssetTool
				.depreciationPerMonthSum(list, AssetType.FIXED_ASSET_TYPE);

		// 科目固定资产 编号 1601
		BigDecimal fixedAsset = fixedAssetTool.fixedAsset(accountId,Global.FIXED_ASSET_NO);

		// 科目累计折旧额

		BigDecimal accumulatedDepreciation = fixedAssetTool
				.accumulatedDepreciation(list, accountId);

		Map<String, Object> resultMap = Maps.newHashMap();
		// 求和资源原值
		if (null != originalVlaueSum) {
			resultMap.put("resourcesOriginal",originalVlaueSum.setScale(Global.KEEP_DIGITS));
		}
		// 资产累计折旧额
		if (null != depreciationPerMonthSum) {
			resultMap.put("assetsAccumulatedDepreciation",depreciationPerMonthSum.setScale(Global.KEEP_DIGITS));
		}
		// 固定资产
		if (null != fixedAsset) {
			resultMap.put("fixedAssets", fixedAsset.setScale(Global.KEEP_DIGITS).doubleValue());
		}
		// 资产累计折旧额
		if (null != accumulatedDepreciation) {
			resultMap.put("accumulatedDepreciation", accumulatedDepreciation.setScale(Global.KEEP_DIGITS));
		}
		// 资产平衡
		if (originalVlaueSum.compareTo(fixedAsset) == 0) {
			resultMap.put("isAssetEquilibria", true);
		} else {
			resultMap.put("isAssetEquilibria", false);
		}
		// 累计平衡
		if (depreciationPerMonthSum.compareTo(accumulatedDepreciation) == 0) {
			resultMap.put("isAccumulatedEquilibria", true);
		} else {
			resultMap.put("isAccumulatedEquilibria", false);
		}
		return resultMap;
	}

	@Override
	@Transactional(readOnly = false)
	public int batctAccumulatedByIds(String ids) {

		if (StringUtils.isBlank(ids))
			return 0;

		String[] arrayIds = ids.split(",");

		if (ArrayUtils.isEmpty(arrayIds))
			return 0;

		FixedAsset fixedAsset = null;
		int count = 0;

		for (String id : arrayIds) {
			fixedAsset = dao.get(id);
			if (null == fixedAsset)
				throw new LogicException("参数【" + id + "】不存在固定资产！");

			if (HandedBy(fixedAsset, AssetType.INTANGIBLE_ASSET_TYPE)) {
				count++;
			}
		}
		return count;
	}

	// 折旧费用科目（企业会计准则默认6602.017管理费用-累计折旧，小企业会计准则默认5602.017管理费用-累计折旧）可切换），
	// 累计折旧科目（企业会计准侧默认1601累计折旧；小企业会计准则默认1602累计折旧，可切换）
	// {depreciationFeeSubject：{},depreciationCumulationSubject:{}}
	@Override
	public Map<String, Object> getFeeSubjectAndCumulationSubjectBybookId(String bookId){
		
		AccountingBook accountingBook =	accountingBookDao.get(bookId);
		
		if(null != accountingBook && StringUtils.isNotBlank(accountingBook.getAccountingSystemId())){
			Map<String, Object>  resultMap =  Maps.newHashMap(); 
			
			 if(AccountSystem.SMALL_ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
				 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "6602.017");
				 resultMap.put("depreciationFeeSubject", customerSubject);
				 
				 customerSubject = customerSubjectService.findBySubjectNo(bookId, "1601");
				 resultMap.put("depreciationCumulationSubject", customerSubject);
				 
			 }else if(AccountSystem.ACCOUNTING_STANDARDS_ID.equals(accountingBook.getAccountingSystemId())){
				 CpaCustomerSubject customerSubject = customerSubjectService.findBySubjectNo(bookId, "5602.017");
				 resultMap.put("depreciationFeeSubject", customerSubject);
				 
				 customerSubject = customerSubjectService.findBySubjectNo(bookId, "1602");
				 resultMap.put("depreciationCumulationSubject", customerSubject);
			 }
			
			 return resultMap;
		}
		return null;
	}

	@Override
	public BigDecimal getDepreciationPerMonth(Date depreciationStartDate,
			BigDecimal originalValue, BigDecimal originalDepreciation,
			Integer lifecycle, Integer depreciationType, Date period,
			Double residualRate, Integer type) {

		if (null == originalValue || null == depreciationType
				|| null == lifecycle || type == null)
			return null;

		// 平均年限法
		if (FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR == depreciationType) {
			return MathUtils.getDepreciationTypeYear(residualRate, lifecycle,
					originalValue, Global.KEEP_DIGITS);

		} else if (FixedAsset.DEPRECIATION_TYPE_DOUBLE_BALANCE_DECLINE == depreciationType) {
			// 双倍余客递减
			return MathUtils.getDepreciationTypeDouble(residualRate,
					depreciationStartDate, originalDepreciation, lifecycle,
					originalValue, period, Global.KEEP_DIGITS, type);

		} else if (FixedAsset.DEPRECIATION_TYPE_ONE_DEPRECIATION == depreciationType
				|| FixedAsset.DEPRECIATION_TYPE_ONE_AMORTIZATION == depreciationType) {
			// 一次提足折旧;一产欠摊销
			return MathUtils.getDepreciationTypeOne(residualRate,
					originalValue, Global.KEEP_DIGITS, type);

		} else if (FixedAsset.DEPRECIATION_TYPE_NO_AMORTIZATION == depreciationType
				|| FixedAsset.DEPRECIATION_TYPE_NO_DEPRECIATION == depreciationType) {
			return BigDecimal.ZERO;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean HandedBy(FixedAsset fixedAsset, int type) {

		String customerId = fixedAssetTool.getCustomerIdByAccountId(fixedAsset
				.getAccountId());

		if (StringUtils.isNotBlank(customerId)) {

			CpaVoucher voucher = new CpaVoucher();
			voucher.setCustomerId(customerId);
			voucher.setVoucherType(2);// 资产凭证
			voucher.setVoucherDate(new Date());
			voucher.setBookId(fixedAsset.getAccountId());

			String periodStr = CurrentAccountingUtils.getCurrentVoucherPeriod(fixedAsset
					.getAccountId());
			Date period = DateUtils.parseDate(periodStr);
			fixedAssetTool.getVoucherSubjectByFixedAsset(fixedAsset, voucher,
					period, type);

			// 插入凭证
			@SuppressWarnings("unchecked")
			Map<String, Object> voucherMap = (Map<String, Object>) voucherService
					.insertCpaVoucher(voucher);

			if (null == voucherMap
					|| !Boolean.parseBoolean(String.valueOf(voucherMap
							.get("state")))) {
				throw new LogicException("插入凭证失败！");
			} else {
				fixedAsset.setStatus(FixedAsset.STATUS_ALREADY_CLEAN_OR_HANDE);
				fixedAsset.setVoucherId(String.valueOf(voucherMap.get("id")));
				fixedAsset.setClearDate(new Date());
				dao.update(fixedAsset);
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Map<String, Object>> findAssetNameList(Date periodFrom,
			Date periodTo, String accountId, Integer type, String assetTypeId) {

		if (null != type && (AssetType.APPORTIONED_ASSET_TYPE != type && StringUtils.isBlank(assetTypeId))) {// 长期摊销
			throw new LogicException("资产类别不能为空！");
		}
		Map<String, Object> queryMap = Maps.newHashMap();

		queryMap.put("periodFrom", periodFrom);
		queryMap.put("periodTo", periodTo);
		queryMap.put("accountId", accountId);
		queryMap.put("assetTypeId", assetTypeId);
		queryMap.put("status", FixedAsset.STATUS_CLOSE_ACCOUNTS);
		queryMap.put("type", type);

		return dao.findAssetNameList(queryMap);
	}

	
	public List<DepreciationDetail> findAssetListByCodesAndPeriodAndType(
			String bookId,String periodFromStr, String periodToStr,
			String assetType, Integer type, List<String> codes) {

		if (StringUtils.isBlank(bookId)||StringUtils.isBlank(periodFromStr)
				|| StringUtils.isBlank(periodToStr)|| CollectionUtils.isEmpty(codes) 
				|| null == type) {
			return null;
		}

		List<? extends FixedAsset> list = null;
		if (AssetType.FIXED_ASSET_TYPE == type) {// 查询固定资产列表
			list = dao.findSingleFixedAssetListByMap(this.getQueryMap(periodFromStr,
					periodToStr, assetType, codes,bookId));
		} else if (AssetType.INTANGIBLE_ASSET_TYPE == type) {// 查询无形资产列表
			list = intangibleAssetDao.findIntangibleAssetListByMap(this
					.getQueryMap(periodFromStr, periodToStr, assetType, codes,bookId));
		} else if (AssetType.APPORTIONED_ASSET_TYPE == type) {// 查询长期摊销列表
			list = longApportionedAssetsDao.findLongApportioneAssetListByMap(this.getQueryMap(
					periodFromStr, periodToStr, assetType, codes,bookId));
		}
		return getDepreciationDetailListByFixedList(list,assetType,type);
	}

	private List<DepreciationDetail> getDepreciationDetailListByFixedList(
			List<? extends FixedAsset> list, String assetTypeId,int type) {

		if (CollectionUtils.isEmpty(list))
			return null;

		AssetType assetType = null;
		if(StringUtils.isNotBlank(assetTypeId)){
			assetType = assetTypeDao.get(assetTypeId);
		}
		
		DepreciationDetail depreciationDetail = new DepreciationDetail(); 
		
		if (null != assetType) {
			depreciationDetail.setAssetType(assetType);
		}

		depreciationDetail.setAssetClass(type);
		if (type <= AssetType.ASSET_NAME.length) {
			depreciationDetail.setAssetClassName(AssetType.ASSET_NAME[type-1]);
		}
		
		List<DepreciationDetail> returnList = Lists.newArrayList();
		
		Iterator<? extends FixedAsset> iterable = list.iterator();
		FixedAsset fixedAsset = null;
		DepreciationDetail detail = null;
		String currentCode = "";
		while (iterable.hasNext()) {
			fixedAsset = iterable.next();
			
			if (StringUtils.isBlank(currentCode)) {
				currentCode = fixedAsset.getCode();
				depreciationDetail.setAssetCode(currentCode);
				depreciationDetail.setAssetName(fixedAsset.getName());
				detail = getNewDepreciationDetailByOld(depreciationDetail);
				returnList.add(detail);// 获得新的
			}
			if (currentCode.equals(fixedAsset.getCode())) {
				detail.getAssetList().add(fixedAsset);
			} else if (!currentCode.equals(fixedAsset.getCode())) {
				currentCode = fixedAsset.getCode();
				depreciationDetail.setAssetCode(currentCode);
				depreciationDetail.setAssetName(fixedAsset.getName());
				detail = getNewDepreciationDetailByOld(depreciationDetail);
				returnList.add(detail);// 获得新的
				detail.getAssetList().add(fixedAsset);
			}

		}

		//组织结果
		returnList.stream().forEach(a -> {
			a.addTotal();
		});

		return returnList;
	}

	private DepreciationDetail getNewDepreciationDetailByOld(
			DepreciationDetail depreciationDetail) {
		DepreciationDetail detail = new DepreciationDetail();
		detail.setAssetClass(depreciationDetail.getAssetClass());
		detail.setAssetClassName(depreciationDetail.getAssetClassName());
		detail.setAssetType(depreciationDetail.getAssetType());
		detail.setAssetCode(depreciationDetail.getAssetCode());
		detail.setAssetName(depreciationDetail.getAssetName());

		return detail;
	}

	/**
	 * 
	 * @Description: TODO(findAssetListByCodesAndPeriodAndType 封装参数)
	 * @param periodFromStr
	 * @param periodToStr
	 * @param assetType
	 * @param type
	 * @param codes
	 * @param bookId
	 * @return Map<String,Object> 返回类型
	 * @throws
	 * @author lixc
	 * @date 2017年12月19日
	 */
	private Map<String, Object> getQueryMap(String periodFromStr,
			String periodToStr, String assetType, List<String> codes,String bookId) {
		Map<String, Object> queryMap = Maps.newHashMap();
		queryMap.put("codes", codes);
		queryMap.put("assetTypeId", assetType);
		queryMap.put("status", FixedAsset.STATUS_CLOSE_ACCOUNTS);
		queryMap.put("periodFrom", DateUtils.parseDate(periodFromStr));
		queryMap.put("accountId", bookId);
		queryMap.put("periodTo", DateUtils.parseDate(periodToStr));
		queryMap.put("orderStr", "a.code , a.create_date");

		return queryMap;
	}

	@Override
	@Transactional(readOnly = false)
	public int updateBatchStatusByVoucherIds(String[] VoucherIds, int status) {

		if (ArrayUtils.isEmpty(VoucherIds))
			return 0;

		List<FixedAsset> list = Lists.newArrayList();
		FixedAsset asset = null;
		for (int i = 0; i < VoucherIds.length; i++) {
			asset = new FixedAsset();
			asset.setVoucherId(VoucherIds[i]);
			asset.setStatus(status);
			this.preUpdate(asset);
			list.add(asset);
		}
		// 更改状态 主要为更新updateBy 和 updateDate
		return dao.updateBatchFixedAssetByVourcherIds(list);
	}


	public BigDecimal sumThisMonthDepreciation(String bookId,int type){
		
		if(StringUtils.isBlank(bookId)) return null;
		//获得当前账期
		String currentPriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		
	     Map<String, Object> queryMap = new HashMap<String, Object>();
	     queryMap.put("accountId", bookId);
	     queryMap.put("currentPeriod", DateUtils.parseDate(currentPriod));
	     queryMap.put("status", FixedAsset.STATUS_NORMAL);
	     
		if (AssetType.FIXED_ASSET_TYPE == type) {// 统计固定资产当月折旧
			
			List<FixedAsset> fixedAssetList = dao.sumFixedAssetListByMap(queryMap);
			if(CollectionUtils.isNotEmpty(fixedAssetList)){
			return fixedAssetList.get(0).getThisMonthDepreciation();
			}
			
		} else if (AssetType.INTANGIBLE_ASSET_TYPE == type) {// 统计无形资产当月折旧
 
			List<IntangibleAsset> intangibleAssetList = intangibleAssetDao.sumIntangibleAssetListByMap(queryMap);

			if(CollectionUtils.isNotEmpty(intangibleAssetList)){
				return intangibleAssetList.get(0).getThisMonthDepreciation();
			}
			
		} else if (AssetType.APPORTIONED_ASSET_TYPE == type) {// 统计长期摊销当月折旧
			List<LongApportionedAssets> longList = longApportionedAssetsDao.sumLongApportionedAssetsListByMap(queryMap);
			if(CollectionUtils.isNotEmpty(longList)){
				return longList.get(0).getThisMonthDepreciation();
			}
		}
		return null;
	}
	
	
	public List<CpaVoucherSubject> getSubjectAndSumthisMonthDepreciation(String bookId,int type,String abstractsName){
		
		if(StringUtils.isBlank(bookId)){
			throw new LogicException("账簿不能为空！");
		}
		if(type<1 || type>3){
			throw new LogicException("不存在类型【"+type+"】！");
		}
		
		//1：借 0：贷
		List<CpaVoucherSubject> list = fixedAssetTool.getSubjectAndSumthisMonthDepreciation(bookId, type, true,abstractsName);
		if(CollectionUtils.isEmpty(list)){
			list = fixedAssetTool.getSubjectAndSumthisMonthDepreciation(bookId, type, false,abstractsName);
		}else{
			list.addAll(fixedAssetTool.getSubjectAndSumthisMonthDepreciation(bookId, type, false,abstractsName));
		}
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public int initAssetTypeFromAssetTypeTemplateBybookId(String bookId) {
		
		if(StringUtils.isBlank(bookId)) throw new LogicException("账薄不能为空！");
		
		AssetType assetType = new AssetType();
		assetType.setAccountId(bookId);
		 this.preInsert(assetType);
		return dao.insertAssetTypeByAssetTypeTemplate(assetType);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean  AssetInvoicing(String bookId,Integer type,boolean beforeChangeBookStatus) throws LoginException{
       
		if(StringUtils.isBlank(bookId)){
        	throw new LoginException("账簿不能为空！");
        }
		
		if(null == type || (type.intValue() < 1 || type.intValue() > 3))
		{
			throw new LoginException("资产类型不存在！");
		}
		
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		if(!beforeChangeBookStatus){
			currentPeriod = DateUtils.getPre2Time(currentPeriod, -1);
		}
    	 
    	if(StringUtils.isBlank(currentPeriod)){
    		throw new LoginException("当前账期不能为空！");
    	}
    	
    	boolean falg = false;
		if(AssetType.FIXED_ASSET_TYPE == type){//固定资产
			// 查询当前账期自查列表   状态为正常
			List<FixedAsset> list = dao.findFixedAssetListByMap(getAssetInvoicingQueryMap(bookId, currentPeriod));
			// 更改原有资产状态  为 已结账
			fixedAssetTool.updateStatusByAssetList(list,FixedAsset.STATUS_CLOSE_ACCOUNTS,type);
			//批量插入下月账期
			fixedAssetTool.batchInsertAssetList(list,type);
			falg = true;
		}else if(AssetType.INTANGIBLE_ASSET_TYPE == type){//无形资产
			// 查询当前账期自查列表   状态为正常
			List<IntangibleAsset> list = intangibleAssetDao.findIntangibleAssetListByMap(getAssetInvoicingQueryMap(bookId, currentPeriod));
			// 更改原有资产状态  为 已结账
			fixedAssetTool.updateStatusByAssetList(list,FixedAsset.STATUS_CLOSE_ACCOUNTS,type);
			//批量插入下月账期
			fixedAssetTool.batchInsertAssetList(list,type);
			falg = true;
		}else if(AssetType.APPORTIONED_ASSET_TYPE == type){//长期摊销
			// 查询当前账期自查列表   状态为正常
			List<LongApportionedAssets> list = longApportionedAssetsDao.findLongApportioneAssetListByMap(getAssetInvoicingQueryMap(bookId, currentPeriod));
			// 更改原有资产状态  为 已结账
			fixedAssetTool.updateStatusByAssetList(list,FixedAsset.STATUS_CLOSE_ACCOUNTS,type);
			//批量插入下月账期
			fixedAssetTool.batchInsertAssetList(list,type);
			falg = true;
		}
		return falg;
	}
	
	private Map<String, Object>  getAssetInvoicingQueryMap(String bookId,String currentPeriod){
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("accountId", bookId);
		queryMap.put("currentPeriod", currentPeriod);
		queryMap.put("status", FixedAsset.STATUS_NORMAL);
		return queryMap;
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, Object> importListData(List<FixedAsset> list, String bookId) {
		Map<String, Object> maps= new HashMap<String, Object>();
		List<FixedAsset> importFixedAssets= new ArrayList<FixedAsset>();
		List<String> errorMsgList = new ArrayList<String>();
		String periodStr = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
 		Date period = DateUtils.parseDate(periodStr);
		int num=0;
		for (FixedAsset fixedAsset : list) {
			num++;
			if(!importVerificationFixedAsset(fixedAsset,errorMsgList,num,period)){
				continue;
			}
			fixedAsset.setAssetTypeId(importSaveAssetType(bookId,fixedAsset.getResidualRate(),fixedAsset.getAssetTypeName(), fixedAsset.getLifecycle()));
			Map<String, Object> defaultSubjectIdMap=this.getFeeSubjectAndCumulationSubjectBybookId(bookId);
			CpaCustomerSubject depreciationCumulationSubject =(CpaCustomerSubject)defaultSubjectIdMap.get("depreciationCumulationSubject");
		    fixedAsset.setDepreciationCumulationSubject(depreciationCumulationSubject);
		    CpaCustomerSubject depreciationFeeSubject =(CpaCustomerSubject)defaultSubjectIdMap.get("depreciationFeeSubject");
			fixedAsset.setDepreciationCumulationSubject(depreciationCumulationSubject);
			fixedAsset.setDepreciationFeeSubject(depreciationFeeSubject);
			fixedAsset.setAccountId(bookId);
			fixedAsset.setStatus(FixedAsset.STATUS_NORMAL);
			fixedAsset.setCode(fixedAssetTool.nextCode(bookId,AssetType.FIXED_ASSET_TYPE));
			AssetUtils.changeSave(fixedAsset);//处理 净值 本月结账  入账日期
			log.info("==========================>>"+JSON.toJSONString(fixedAsset));
            save(fixedAsset);
		}
		maps.put("errorMsgList", errorMsgList);
		maps.put("successTotal", importFixedAssets.size());
		return maps;
	}
	/**
	 * 导入添加固定资产类型
	 * @param bookId
	 * @param residualRate
	 * @param name
	 * @param lifecycle
	 */
	private String importSaveAssetType(String bookId, Double residualRate,String name, Integer lifecycle) {
		AssetType assetType = new AssetType();
		assetType.setAccountId(bookId);
		assetType.setType(AssetType.FIXED_ASSET_TYPE);
		List<AssetType> assetTypeList= assetTypeDao.findList(assetType); //查询资产类型
        for (AssetType at : assetTypeList) {
         	if(name.equals(at.getName())){
         	  return at.getId();
         	}
		} 
		assetType.setResidualRate(String.valueOf(residualRate));
		assetType.setLifecycle(lifecycle);
		assetType.setName(name); 
        assetTypeService.saveAssetType(assetType);
     	return assetType.getId(); 
	}

	/**
	 * 验证组装需要导入固定资产数据
	 * @param fixedAsset   固定资产对象
	 * @param errorMsgList 异常数据
	 * @param num   条数 
	 * @param period 
	 * @return
	 */
	private boolean importVerificationFixedAsset(FixedAsset fixedAsset,List<String> errorMsgList, int num,Date period){
		if(StringUtils.isBlank(fixedAsset.getAssetTypeName())){
			errorMsgList.add("第"+num+"行数据资产类型为空！");
			return false;
		}
        if(StringUtils.isBlank(fixedAsset.getName())){
        	errorMsgList.add("第"+num+"行数据设备名称为空！");
        	return false;
        }
        if(fixedAsset.getOriginalValue()==null){
        	errorMsgList.add("第"+num+"行数据原值为空！");
        	return false;
        }
        if(fixedAsset.getOriginalValue()==null){
        	errorMsgList.add("第"+num+"行数据原值为空！");
        	return false;
        }
        if(fixedAsset.getResidualRate()==null){
        	errorMsgList.add("第"+num+"行数据残值率为空！");
        	return false;
        }
        if(fixedAsset.getLifecycle()==null){
        	errorMsgList.add("第"+num+"行数据使用期限为空！");
        	return false;
        }
        if(fixedAsset.getOriginalDepreciation()==null){
        	errorMsgList.add("第"+num+"行数据期初累计折旧为空！");
        	return false;
        }
        //导入默认平均年算法
        fixedAsset.setDepreciationType(FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR);
        //计算月折旧额
       BigDecimal  depreciationPerMonth=getDepreciationPerMonth(fixedAsset.getDepreciationDate(),fixedAsset.getOriginalValue(),fixedAsset.getOriginalDepreciation(),
                   fixedAsset.getLifecycle(),FixedAsset.DEPRECIATION_TYPE_AVERAGING_YEAR,period,fixedAsset.getResidualRate(),AssetType.FIXED_ASSET_TYPE);
        fixedAsset.setDepreciationPerMonth(depreciationPerMonth);
        Calendar c = Calendar.getInstance();
        c.setTime(fixedAsset.getAccountedForDate());
        c.add(Calendar.MONTH, 2);
        fixedAsset.setDepreciationStartDate(c.getTime()); //起始结账日期
        fixedAsset.setCurrentPeriod(period);
        return true;
	}
}
