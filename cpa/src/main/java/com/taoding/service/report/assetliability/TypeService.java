package com.taoding.service.report.assetliability;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.mapper.report.assetliability.TypeDao;
import com.taoding.domain.report.assetliability.Catalogue;
import com.taoding.domain.report.assetliability.Type;


public interface TypeService extends CrudService<TypeDao, Type>{

	/**
	 * 根据会计准则获取所有的资产负债表类型以及子项目和模板公式
	 * @param accountRuleId accountRuleId
	 * @return list
	 */
	List<Type> getAssetLiabilityTypeByAccountRule(int accountRuleId);
	
	/**
	 * 根据账簿id获取所有资产负债表类型以及子项目和模板公式
	 * @param accountId
	 * @return list
	 */
	List<Catalogue> getAssetLiabilityTypeByAccountIdAndAccountRuleId(String accountId, Integer accountRuleId);
	
}
