package com.taoding.service.voucher;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.domain.voucher.CpaVoucherTempleteSubject;
import com.taoding.mapper.voucher.CpaVoucherTempleteSubjectDao;

/**
 * 凭证模板科目
 * 
 * @author czy 2017年11月28日 下午12:01:05
 */
@Service
public class CpaVoucherTempleteSubjectServiceImpl
		extends
		DefaultCurdServiceImpl<CpaVoucherTempleteSubjectDao, CpaVoucherTempleteSubject>
		implements CpaVoucherTempleteSubjectService {

	/**
	 * 批量新增模板科目
	 * 
	 * @param lists
	 * @return
	 */
	@Override
	@Transactional
	public int batchInsert(List<CpaVoucherTempleteSubject> lists) {
		return dao.batchInsert(lists);
	}

	/**
	 * 根据凭证模板ID删除模板科目 2017年11月28日 下午4:43:45
	 * 
	 * @param templeteId
	 * @return
	 */
	@Override
	@Transactional
	public int deleteByTempleteId(String templeteId) {
		return dao.deleteByTempleteId(templeteId);
	}

	/**
	 * 通过模板id查询模板科目 2017年11月28日 下午6:11:34
	 * 
	 * @param templeteId
	 * @return
	 */
	@Override
	public List<CpaVoucherTempleteSubject> findByTempleteId(String bookId,
			String templeteId) {
		List<CpaVoucherTempleteSubject> lists = dao.findByTempleteId(bookId, templeteId);
		if (lists != null && lists.size() > 0) {
			for (CpaVoucherTempleteSubject templeteSubject : lists) {
				BigDecimal surplusAmount;
				if (templeteSubject.isDirection()) { // 借方  余额 = 期初  + 借方 - 贷方
					surplusAmount = templeteSubject.getBeginningBalances()
							.add(templeteSubject.getSumDebit())
							.subtract(templeteSubject.getSumCredit());
				} else { // 贷方  余额 = 期初  + 贷方 - 借方
					surplusAmount = templeteSubject.getBeginningBalances()
							.add(templeteSubject.getSumCredit())
							.subtract(templeteSubject.getSumDebit());
				}
				templeteSubject.setSurplusAmount(surplusAmount);
			}
		}
		return lists;
	}

	/**
	 * 通过科目id查询所有记账凭证所有科目信息
	 * 2017年12月1日 下午3:06:24
	 * @param bookId
	 * @param subjectId
	 * @return
	 */
	@Override
	public List<CpaVoucherTempleteSubject> findBySubjectId(String bookId,
			String subjectId) {
		return dao.findBySubjectId(bookId, subjectId);
	}

	
	
}
