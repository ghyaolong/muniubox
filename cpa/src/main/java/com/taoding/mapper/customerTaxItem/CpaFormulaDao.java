package com.taoding.mapper.customerTaxItem;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerTaxItem.CpaFormula;

/**
 * 税项模板公式Dao
 * 
 * @author mhb
 * @version 2017-11-27
 */
@Repository
@Mapper
public interface CpaFormulaDao extends CrudDao<CpaFormula> {
	/**
	 * 批量查入税项模板公式
	 * 
	 * @param cpaFormulaList
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	int insertList(@Param("cpaFormulaList")List<CpaFormula> cpaFormulaList);
	/**
	 * 根据税项模板id查询对应的税项模板公式
	 * 
	 * @param taxTemplateId 税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	List<CpaFormula> findCustomerTaxtemplateByTaxTemplateId(String taxTemplateId);

	 

}

