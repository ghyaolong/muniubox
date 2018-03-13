package com.taoding.service.voucher;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.voucher.CpaVoucherTemplete;
import com.taoding.mapper.voucher.CpaVoucherTempleteDao;

/**
 * 凭证模板
 * 
 * @author czy 2017年11月28日 上午11:52:55
 */
public interface CpaVoucherTempleteService extends
		CrudService<CpaVoucherTempleteDao, CpaVoucherTemplete> {
	
	
	/**
	 * 查询 企业所保存的凭证模板
	 * 2017年11月28日 下午2:31:00
	 * @param bookId
	 * @param type
	 * @return
	 */
	public List<CpaVoucherTemplete> findListByBookId(String bookId,boolean type);
	
	/**
	 * 通过模板id查询模板及模板科目信息
	 * 2017年11月28日 下午5:33:47
	 * @param id
	 * @return
	 */
	public CpaVoucherTemplete findById(String id);
	
	/**
	 * 新增凭证模板
	 * 2017年11月28日 下午2:50:55
	 * @param voucherTemplete
	 * @return
	 */
	public Object insertVoucherTemplete(CpaVoucherTemplete voucherTemplete);
	
	/**
	 * 根据模板名称查询-检索模板名称唯一性
	 * 2017年11月28日 下午3:28:10
	 * @param bookId
	 * @param templeteName
	 * @return
	 */
	public CpaVoucherTemplete findByTempleteName(String bookId,String templeteName);
	
	/**
	 * 根据模板id删除凭证模板
	 * 2017年11月28日 下午4:39:22
	 * @param id
	 * @return
	 */
	public Object deleteById(String id);
}
