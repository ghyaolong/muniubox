package com.taoding.service.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.checkoutSetting.SettleAccountOperatingSetting;
import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingData;
import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingDataExample;
import com.taoding.domain.settleaccount.CpaFinalLiquidationOperatingDataExample.Criteria;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationOperatingDataDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.checkoutSetting.SettleAccountOperatingSettingService;

@Service
@Transactional
public class CpaFinalLiquidationOperatingDataServiceImpl implements CpaFinalLiquidationOperatingDataService {
	@Autowired
	private CpaFinalLiquidationOperatingDataDao operatingDataDao;
	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private CpaFinalLiquidationOperatingDataService operatingDataService;
	@Autowired
	private SettleAccountOperatingSettingService operatingSettingService;
	
	
	/**
	 * 加载 结账经营数据分析
	 * 2017年12月27日 下午5:05:44
	 * @param bookId
	 * @return
	 */
	@Override
	public Object loadOperatingData(String bookId) {
		Set<Map<String, Object>> result = new HashSet<>();
		
		List<SettleAccountOperatingSetting> settings = operatingSettingService.findSettleAccountOperatingListData(bookId);
		
		OperatingDataType[] values = OperatingDataType.values();
		List<CpaFinalLiquidationOperatingData> datas;
		SettleAccountOperatingSetting setting;
		boolean isNormal = false;
		Map<String, Object> data;
		for (OperatingDataType dataType : values) {
			data = new HashMap<>();
			setting = getSettingByName(settings, dataType.getName());
			
			if (dataType == OperatingDataType.CUMULATIVE_INCOME) {
				datas = operatingDataService.getOperatingDatas(bookId, dataType, -10);
			} else {
				datas = operatingDataService.getOperatingDatas(bookId, dataType, -6);
			}
			
			if (dataType == OperatingDataType.CUMULATIVE_INCOME) {
				BigDecimal totalAmount = new BigDecimal("0");
				for (CpaFinalLiquidationOperatingData operatingData : datas) {
					totalAmount = totalAmount.add(operatingData.getProportion());
				}
				//isNormal = totalAmount.compareTo(setting.getStartNum()) == -1;
			} else if (dataType == OperatingDataType.TAX_VALUE) {
				for (CpaFinalLiquidationOperatingData operatingData : datas) {
					if (operatingData.getProportion().compareTo(new BigDecimal("0")) == 1) {
						isNormal = true;
					}
				}
			} else {
				BigDecimal avg = operatingDataService.avgOfOperatingDatas(datas);
				//isNormal = avg.compareTo(setting.getStartNum()) == 1 && avg.compareTo(setting.getEndNum()) == -1;
			}
			
			List<Map<String, Object>> list = new ArrayList<>();
			for (CpaFinalLiquidationOperatingData operatingData : datas) {
				Map<String, Object> map = new HashMap<>();
				map.put("accountDate", operatingData.getCurrentPeriod());
				map.put("value", operatingData.getProportion());
				list.add(map);
			}
			data.put("data", list);
			data.put("isNormal", isNormal);
			
			result.add(data);
		}
		return result;
	}

	
	private SettleAccountOperatingSetting getSettingByName(List<SettleAccountOperatingSetting> settings, String name) {
		for (SettleAccountOperatingSetting setting : settings) {
			if (setting.getSubName().equals(name)) {
				return setting;
			}
		}
		return null;
	}
	
	
	@Override
	public void insertOperatingData(String bookId, OperatingDataType type, BigDecimal value, Date accountDate) {
		AccountingBook accountingBook = accountingBookService.get(bookId);
		if (accountingBook == null) {
			throw new LogicException("无此账簿!");
		}
		insertOperatingData(bookId, accountingBook.getCustomerInfoId(), type, value, accountDate);
	}

	@Override
	public void insertOperatingData(String bookId, String customerId, OperatingDataType type, BigDecimal value, Date accountDate) {
		if (StringUtils.isBlank(bookId) 
				|| StringUtils.isBlank(type.toString())
				|| value == null
//				|| value.compareTo(new BigDecimal("0")) < 0 
//				|| value.compareTo(new BigDecimal("1")) >= 0
				|| accountDate == null) {
			throw new LogicException("参数异常!");
		}
		
		CpaFinalLiquidationOperatingData operatingData = new CpaFinalLiquidationOperatingData();
		operatingData.setId(UUIDUtils.getUUid());
		operatingData.setBookId(bookId);
		operatingData.setCustomerId(customerId);
		operatingData.setCurrentPeriod(accountDate);
		operatingData.setSubKey(type.name());
		operatingData.setProportion(value);
		operatingData.setCreateDate(new Date());
		operatingData.setCreateBy(UserUtils.getCurrentUserId());
		operatingDataDao.insert(operatingData);
	}

	@Override
	public List<CpaFinalLiquidationOperatingData> getOperatingDatas(String bookId, OperatingDataType type, int preMonth) {
		Date accountDate = DateUtils.parseDate(CurrentAccountingUtils.getCurrentVoucherPeriod(bookId));
		
		CpaFinalLiquidationOperatingDataExample example = new CpaFinalLiquidationOperatingDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		criteria.andSubKeyEqualTo(type.name());
		criteria.andCurrentPeriodGreaterThanOrEqualTo(DateUtils.getPreTime(accountDate, preMonth));
		criteria.andCurrentPeriodLessThanOrEqualTo(accountDate);
		example.setOrderByClause("current_period ASC");
		return operatingDataDao.selectByExample(example);
	}

	@Override
	public BigDecimal avgOfOperatingDatas(List<CpaFinalLiquidationOperatingData> datas) {
		BigDecimal total = new BigDecimal("0");
		int count = 0;
		if(datas != null && datas.size() > 0){
			for (CpaFinalLiquidationOperatingData data : datas) {
				total.add(data.getProportion());
				count++;
			}
		}
		if(count == 0){
			return new BigDecimal(0);
		}
		return total.divide(new BigDecimal(count));
	}
	
	@Override
	public void deleteOperatingData(String bookId, Date accountDate) {
		CpaFinalLiquidationOperatingDataExample example = new CpaFinalLiquidationOperatingDataExample();
		Criteria criteria = example.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		criteria.andCurrentPeriodEqualTo(accountDate);
		operatingDataDao.deleteByExample(example);
	}
	
}
