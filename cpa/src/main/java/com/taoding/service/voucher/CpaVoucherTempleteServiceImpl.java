package com.taoding.service.voucher;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.NumberToCN;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.user.User;
import com.taoding.domain.voucher.CpaVoucherTemplete;
import com.taoding.domain.voucher.CpaVoucherTempleteSubject;
import com.taoding.mapper.voucher.CpaVoucherTempleteDao;


/**
 * 凭证模板
 * @author czy
 * 2017年11月28日 下午3:08:57
 */
@Service
public class CpaVoucherTempleteServiceImpl extends
		DefaultCurdServiceImpl<CpaVoucherTempleteDao, CpaVoucherTemplete>
		implements CpaVoucherTempleteService {

	@Autowired
	private CpaVoucherTempleteSubjectService  templeteSubjectService ;
	
	/**
	 * 查询 企业所保存的凭证模板
	 * 2017年11月28日 下午2:31:00
	 * @param bookId
	 * @param type
	 * @return
	 */
	@Override
	public List<CpaVoucherTemplete> findListByBookId(String bookId,boolean type) {
		return dao.findListByBookId(bookId,type);
	}

	/**
	 * 通过模板id查询模板及模板科目信息
	 * 2017年11月28日 下午5:33:47
	 * @param id
	 * @return
	 */
	@Override
	public CpaVoucherTemplete findById(String id) {
		CpaVoucherTemplete voucherTemplete = dao.get(id);
		if(voucherTemplete != null && StringUtils.isNotEmpty(voucherTemplete.getId())){
			List<CpaVoucherTempleteSubject> lists = templeteSubjectService.findByTempleteId(voucherTemplete.getBookId(),id);
			if(lists != null && lists.size() > 0){
				voucherTemplete.setSubjectLists(lists);
			}
		}
		return voucherTemplete;
	}

	/**
	 * 新增凭证模板
	 * 2017年11月28日 下午2:50:55
	 * @param voucherTemplete
	 * @return
	 */
	@Override
	@Transactional
	public Object insertVoucherTemplete(CpaVoucherTemplete voucherTemplete) {
		String id = UUIDUtils.getUUid();
		//是否需要保存金额
		boolean haveMoney = voucherTemplete.isHaveMoney();
		boolean type = voucherTemplete.isType(); //true 模板   false 草稿
		
		if(type){
			CpaVoucherTemplete templete = this.findByTempleteName(voucherTemplete.getBookId(), voucherTemplete.getTempleteName());
			if(templete != null && StringUtils.isNotEmpty(templete.getId())){
				throw new LogicException("模板名称已经存在");
			}
		}
		
		List<CpaVoucherTempleteSubject> subjectLists = voucherTemplete.getSubjectLists();
		BigDecimal amountDebit = new BigDecimal(0);
		BigDecimal amountCredit = new BigDecimal(0);
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		if(subjectLists != null && subjectLists.size() > 0){
			for (CpaVoucherTempleteSubject templeteSubject : subjectLists) {
				if(templeteSubject != null){
					if(templeteSubject.getAmountDebit() != null){
						amountDebit = amountDebit.add(templeteSubject.getAmountDebit());
					}
					if(templeteSubject.getAmountCredit() != null){
						amountCredit = amountCredit.add(templeteSubject.getAmountCredit());
					}
					if(type){
						if(!haveMoney){ //不需要保存金额 把提交的科目金额置为null
							templeteSubject.setAmountDebit(null);
							templeteSubject.setAmountCredit(null);
						}
					}
					templeteSubject.setId(UUIDUtils.getUUid());
					templeteSubject.setTempleteId(id);
					templeteSubject.setCustomerId(voucherTemplete.getCustomerId());
					templeteSubject.setBookId(voucherTemplete.getBookId());
					templeteSubject.setCreateDate(new Date());
					templeteSubject.setCreateBy(createBy);
				}
			}
		}
		
		int countTemplete = templeteSubjectService.batchInsert(subjectLists);
		if(countTemplete > 0){
			voucherTemplete.setId(id);
			if(haveMoney || !type){ //需要保存金额
				voucherTemplete.setAmountDebit(amountDebit);
				voucherTemplete.setAmountCredit(amountCredit);
				voucherTemplete.setAccountCapital(NumberToCN.number2CNMontrayUnit(amountDebit));
			}
			if(!type){
				voucherTemplete.setTempleteName(subjectLists.get(0).getAbstracts() +" " + DateUtils.formatDateTime(new Date()));
			}
			voucherTemplete.setCreateDate(new Date());
			voucherTemplete.setCreateBy(createBy);
			int count = dao.insert(voucherTemplete);
			if(count > 0){
				return true ;
			}
		}
		return false;
	}

	/**
	 * 根据模板名称查询-检索模板名称唯一性
	 * 2017年11月28日 下午3:28:10
	 * @param bookId
	 * @param templeteName
	 * @return
	 */
	@Override
	public CpaVoucherTemplete findByTempleteName(String bookId, String templeteName) {
		return dao.findByTempleteName(bookId, templeteName);
	}

	/**
	 * 根据模板id删除凭证模板
	 * 2017年11月28日 下午4:39:22
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public Object deleteById(String id) {
		int countSubject = templeteSubjectService.deleteByTempleteId(id);
		if(countSubject > 0){
			int count = dao.delete(id);
			if(count > 0){
				return true ;
			}
		}
		return false;
	}
	
}
