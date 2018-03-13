package com.taoding.controller.accountingBook;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.accountingbook.Authorise;
import com.taoding.service.accountingBook.AccountingBookService;
/**
 * 
 * @author lixc
 * @2017年11月20日09:33:33
 *
 */
@RestController
@RequestMapping("/accountingBook")
public class AccountingBookController {
	
	@Autowired
	private AccountingBookService accountingBookService;
	/**
	 * 
	* @Description: 指派/重新指派
	* @param accountingId 	指派会计ID
	* @param ccountingBookId 账簿ID
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	@PostMapping("/accountingBookAssignment")
	public Object accountingBookAssignment(
			 @RequestParam("accountingId")String accountingId,
			 @RequestParam("accountingBookId") String accountingBookId){
		accountingBookService.accountingBookAssignment(accountingId,accountingBookId,Authorise.AUTHORISE_TYPE_ASSIGN,0);
		return true;
	}
	
	/**
	* @Description: TODO(授权) 
	* @param accountingIds
	* @param accountingBookId
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	@PostMapping("/accountingBookAuthorization")
	public Object accountingBookAuthorization(
			@RequestParam("accountingIds")String accountingIds,
			@RequestParam("accountingBookId")String accountingBookId){
		accountingBookService.accountingBookAssignment(accountingIds,accountingBookId,Authorise.AUTHORISE_TYPE,5);
		return true;
	}
	
	/**
	 * 
	* @Description: TODO(获得会计制度列表) 
	* @return Ojbect 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	@GetMapping("/getAccountSystemList")
	public Object getAccountSystemList(){
		return accountingBookService.findAccountSystemList(null);
	}
	
	
	/**
	* @Description: TODO(纳税人性质列表) 
	* @return Ojbect 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	@GetMapping("/getTaxpayerPropertyList")
	public Object getTaxpayerPropertyList(){
		return accountingBookService.findTaxpayerPropertyList(null);
	}
	
	/**
	 * 
	* @Description: TODO(账簿保存) 
	* @param accountingBook
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	@PostMapping("/saveAccountingBook")
 	public Object saveAccountingBook(@RequestBody AccountingBook accountingBook){
	
		accountingBookService.saveAccountingBook(accountingBook);
		return true;
	}
	
	/**
	 * 获取当前账簿做账区间
	 * 2017年12月11日 上午9:37:34
	 * @param bookId
	 * @return
	 */
	@GetMapping("/getAccountingPeriod/{id}")
	public Object getAccountingPeriod(@PathVariable("id") String bookId){
		if(StringUtils.isNotEmpty(bookId)){
			return accountingBookService.getAccountingPeriod(bookId);
		}
		throw new LogicException("账簿ID不能为空");
	}
}
