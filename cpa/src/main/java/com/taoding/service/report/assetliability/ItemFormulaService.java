package com.taoding.service.report.assetliability;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.report.assetliability.ItemFormula;
import com.taoding.mapper.report.assetliability.ItemFormulaDao;


/**
 * 资产负债表的公式
 * @author vincent
 *
 */
public interface ItemFormulaService extends CrudService<ItemFormulaDao, ItemFormula>{
	

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

}
