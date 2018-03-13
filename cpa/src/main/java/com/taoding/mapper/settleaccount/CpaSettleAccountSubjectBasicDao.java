package com.taoding.mapper.settleaccount;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.domain.settleaccount.CpaSettleAccountSubjectBasic;
import com.taoding.domain.subject.CpaCustomerSubject;

/**
 * 结算科目基础
 * @author admin
 *
 */
@Repository
@Mapper
public interface CpaSettleAccountSubjectBasicDao {
	
	/**
	 * 根据企业类型和 subKey 获取科目 id 
	 * @param enterpriseType 企业类型
	 * @param subKey 唯一 key
	 * @return
	 */
	public List<CpaSettleAccountSubjectBasic> getByEnterpriseTypeAndSubKey(@Param("enterpriseType")String enterpriseType, 
			@Param("subKey")String subKey);
	
	/**
	 * 根据科目 id 查期初，返回 年初余额
	 * @param subjectId
	 * @return
	 */
	public CpaCustomerSubject getCpaCustomerSubjectById(@Param("subjectId")String subjectId, @Param("bookId")String bookId);
	
	
	
	
}
