package com.taoding.service.subject;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.subject.CertificateVO;
import com.taoding.domain.voucher.CpaVoucher;
import com.taoding.mapper.subject.CpaVoucherImportDao;

/**
 * 凭证导入
 */
public interface CpaVoucherImportService extends CrudService<CpaVoucherImportDao, CpaVoucher>{
	/**
	 * 导入凭证
	 * 进行批量插入凭证
	 * 批量插入 凭证科目
	 * @param list VO
	 * @param customerId 客户 id
	 * @param bookId 账薄 id
	 * @return
	 */
	public Object savaCertificateData(List<CertificateVO> list, String customerId, String bookId);

}
