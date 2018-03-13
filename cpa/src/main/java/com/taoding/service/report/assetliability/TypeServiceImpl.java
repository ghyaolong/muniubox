package com.taoding.service.report.assetliability;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.report.assetliability.Catalogue;
import com.taoding.domain.report.assetliability.Type;
import com.taoding.mapper.report.assetliability.TypeDao;

@Service
@Transactional
public class TypeServiceImpl extends DefaultCurdServiceImpl<TypeDao, Type> implements TypeService{

	@Override
	public List<Type> getAssetLiabilityTypeByAccountRule(int accountRuleId) {
		return dao.getAssetLiabilityTypeByAccountRule(accountRuleId);
	}

	@Override
	public List<Catalogue> getAssetLiabilityTypeByAccountIdAndAccountRuleId(String accountId, Integer accountRuleId) {
		return dao.getAssetLiabilityTypeByAccountIdAndAccountRuleId(accountId, accountRuleId);
	}

}
