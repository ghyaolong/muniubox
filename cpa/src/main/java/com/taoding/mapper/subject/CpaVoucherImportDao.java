package com.taoding.mapper.subject;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.subject.CpaCustomerSubject;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.domain.voucher.CpaVoucherSubject;


/**
 * 凭证导入
 * 
 */
@Repository
@Mapper
public interface CpaVoucherImportDao extends CrudDao<CpaVoucher>{
	
	/**
	 * 根据凭证编号和凭证日期查询
	 * @param voucherNo
	 * @param voucherDate
	 * @return
	 */
	public CpaVoucher findByVoucherNoAndVoucherDate(@Param("voucherNo") String voucherNo, @Param("voucherDate") String voucherDate);

	/**
	 * 根据科目编码去查科目期初
	 * 
	 * @param subjectNo
	 * @return
	 */
	public CpaCustomerSubject getCpaCustomerSubjectBySubjectNo(@Param("subjectNo") String subjectNo, @Param("bookId") String bookId);
	
	/**
	 * 批量新增凭证科目
	 * @param lists
	 * @return
	 */
	public int batchInsertCpaVoucherSubject(List<CpaVoucherSubject> lists);
	
	/**
	 * 批量新增凭证
	 * @param lists
	 * @return
	 */
	public int batchInsertCpaVoucher(List<CpaVoucher> lists);
}
