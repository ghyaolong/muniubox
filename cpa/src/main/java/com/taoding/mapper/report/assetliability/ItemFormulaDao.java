package com.taoding.mapper.report.assetliability;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.report.assetliability.ItemFormula;

@Repository
@Mapper
public interface ItemFormulaDao extends CrudDao<ItemFormula>{

	/**
	 * 批量保存客户公式
	 * @param formulaList
	 * @return 保存的数量 
	 */
	int batchSaveCustomerFormular(List<ItemFormula> formulaList);
	
	/**
	 * 保存客户公式
	 * @param itemFormula
	 * @return 保存的数量
	 */
	int saveCustomerFormular(ItemFormula itemFormula);
	
	/**
	 * 根据Id删除公式
	 * @param id
	 * @return
	 */
	int deleteFormular(int id);
	
	/**
	 * 根据项目id和账簿id获取公式
	 * @param itemId
	 * @param accountId
	 * @return List<ItemFormula>
	 */
	List<ItemFormula> getFormularByItemIdAndaccountingId(@Param("itemId") int itemId, @Param("accountingId") int accountId);
	
	/**
	 * 更新公式 
	 * @param itemFormula
	 * @return
	 */
	int updateFormula(ItemFormula itemFormula);
	
	/**
	 * 删除当前账套下的所有公式
	 * @return
	 */
	int deleteFormulaByAccountId(int accountId);
}
