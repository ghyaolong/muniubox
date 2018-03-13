package com.taoding.common.utils;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.service.accountingBook.AccountingBookService;


/**
 * 当前账簿的工具类
 * @author Yang ji qiang
 *
 */
public class CurrentAccountingUtils {

	
	private static AccountingBookService accountingBookService = SpringContextHolder.getBean(AccountingBookService.class);
	
	/**
	 * 获取账期
	 * 2017年11月30日 上午9:54:00
	 * @param bookId
	 * @return
	 */
	public static String getCurrentVoucherPeriod(String bookId){
		
		AccountingBook accountingBook = accountingBookService.get(bookId);
		if(accountingBook != null && StringUtils.isNotEmpty(accountingBook.getId())){
			String voucherPeriod ;
			if(accountingBook.getAccountStatus() == 5){ //5已完成 获取下个账期数据
				//获取当前账期的下月第一天
				voucherPeriod = DateUtils.getPerFirstDayOfMonth(accountingBook.getCurrentPeriod());
			}else{
				//获取当前账期的当月第一天
				voucherPeriod = DateUtils.getFirstDayOfMonth(accountingBook.getCurrentPeriod());
			}
			return voucherPeriod ;
		}
		throw new LogicException("查询不到账簿ID为："+bookId+"的数据");
	}
	
	
	/**
	 * 根据账簿id获取当前账簿所使用的会计准则
	 * @return
	 */
	public static Integer getCurrentAccountRuleByAccountingBookId(String bookId) {
		AccountingBook accountingBook = accountingBookService.get(bookId);
		if (null != accountingBook) {
			return Integer.valueOf(accountingBook.getAccountingSystemId());
		}
		throw new LogicException("当前账簿id不存在");
	}
	
}
