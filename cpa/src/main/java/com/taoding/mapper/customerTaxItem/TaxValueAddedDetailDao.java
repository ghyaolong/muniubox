package com.taoding.mapper.customerTaxItem;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.customerTaxItem.TaxValueAddedDetail;



/**
 * 客户增值税设置细则Dao
 * @author mhb
 * @version 2017-11-27
 */
@Repository
@Mapper
public interface TaxValueAddedDetailDao extends CrudDao<TaxValueAddedDetail> {
	/**
	 * 批量插入客户增值税细则数据
	 * 
	 * @param taxValueAddedDetailList
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	int insertList(@Param("taxValueAddedDetailList") List<TaxValueAddedDetail> taxValueAddedDetailList);
	/**
	 * 根据账薄id删除客户增值税细则数据
	 * 
	 * @param accountingId 账薄id
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	void deleteAccountingId(String accountingId);
	/**
	 * 根据账薄id查询客户增值税细则数据
	 * 
	 * @param accountingId 账薄id
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	List<TaxValueAddedDetail> getAccountingId(String accountingId);

}

