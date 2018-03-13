package com.taoding.service.report.assetliability;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taoding.domain.report.assetliability.ItemFormula;
import com.taoding.domain.report.assetliability.OperationSource;
import com.taoding.domain.report.assetliability.Type;

/**
 * 向AssetLiabilityController提供的门面类，将所有和资产负债表相关的服务依赖隐藏起来
 * @author Yang Ji Qiang
 *
 */
public interface AssetLiabilityFacadeService {
	
	
	/**
	 * 初始化报表信息
	 * @param accountId
	 * @return
	 */
	boolean init(int accountId, int accoutingRule);
	
	/**
	 * 保存客户公式
	 * @param itemFormula
	 * @return 保存的数量
	 */
	boolean saveCustomerFormular(ItemFormula itemFormula);
	
	/**
	 * 根据Id删除公式
	 * @param id
	 * @return
	 */
	boolean deleteFormularById(int id);
	
	/**
	 * 根据项目id和账簿id获取公式
	 * @param itemId
	 * @param accountId
	 * @return List<ItemFormula>
	 */
	List<ItemFormula> getFormularByItemIdAndaccountingId(int itemId, int accountId);
	
	/**
	 * 根据id更新公式信息
	 * @param id id
	 * @param subjectId 关联科目id,为null时表示不更新该项
	 * @param operation 操作符，如果为null表示不更新该项
	 * @param operationSourceId 取数规则，如果为null表示不更新该项
	 * @return 影响的行数
	 */
	boolean updateFormulaById(int id, String subjectId, Integer operation, Integer operationSourceId);
	
	/**
	 * 重新初始化工式
	 * @param accrountingId
	 * @return
	 */
	boolean reInitFormularByAccountingId(Integer accrountingId);
	
	/**
	 * 根据会计准则获取所有的资产负债表类型以及子项目和模板公式
	 * @param accountRuleId accountRuleId
	 * @return list
	 */
	List<Type> getAssetLiabilityTypeByAccountRule(int accountRuleId);
	
	/**
	 * 根据账期和账簿id获取报表
	 * @param accountId
	 * @return Map，包含两项，其中，balance表示是否是平衡的，report为报表项
	 */
	Map<String, Object> getReportByBookIdAndPeriod(String accountId, Date period);
	
	
	/**
	 * 根据模块id获取相应的取数规则列表
	 * @param modelId
	 * @return
	 */
	List<OperationSource> listOperationSourceListByModelId(Integer modelId);
}
