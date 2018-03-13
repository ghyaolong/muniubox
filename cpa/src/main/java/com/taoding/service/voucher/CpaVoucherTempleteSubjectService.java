package com.taoding.service.voucher;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.voucher.CpaVoucherTempleteSubject;
import com.taoding.mapper.voucher.CpaVoucherTempleteSubjectDao;

/**
 * 凭证模板科目
 * 
 * @author czy 2017年11月28日 上午11:53:06
 */
public interface CpaVoucherTempleteSubjectService extends
		CrudService<CpaVoucherTempleteSubjectDao, CpaVoucherTempleteSubject> {

	/**
	 * 批量新增模板科目
	 * 2017年11月28日 下午4:20:53
	 * @param lists
	 * @return
	 */
	public int batchInsert(List<CpaVoucherTempleteSubject> lists);
	
	
	/**
	 * 根据凭证模板ID删除模板科目
	 * 2017年11月28日 下午4:43:45
	 * @param templeteId
	 * @return
	 */
	public int deleteByTempleteId(String templeteId);
	
	/**
	 * 通过模板id查询模板科目
	 * 2017年11月28日 下午5:37:34
	 * @param bookId
	 * @param templeteId
	 * @return
	 */
	public List<CpaVoucherTempleteSubject> findByTempleteId(String bookId,String templeteId);
	
	
	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午3:06:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	public List<CpaVoucherTempleteSubject> findBySubjectId(String bookId,String subjectId);
}
