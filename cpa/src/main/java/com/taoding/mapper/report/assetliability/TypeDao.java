package com.taoding.mapper.report.assetliability;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.assetliability.Catalogue;
import com.taoding.domain.report.assetliability.Type;

@Repository
@Mapper
public interface TypeDao extends CrudDao<Type>{
	
	/**
	 * 根据会计准则获取所有的资产负债表类型以及子项目和模板公式
	 * @param accountRuleId accountRuleId
	 * @return list
	 */
	List<Type> getAssetLiabilityTypeByAccountRule(int accountRuleId);
	
	/**
	 * 根据账簿id获取所有资产负债表类型以及子项目和公式
	 * @param accountId
	 * @return list
	 */
	List<Catalogue> getAssetLiabilityTypeByAccountIdAndAccountRuleId(@Param("accountId") String accountId, @Param("accountRuleId") int accountRuleId);
	
	
}
