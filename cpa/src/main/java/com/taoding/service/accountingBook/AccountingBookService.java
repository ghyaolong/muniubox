package com.taoding.service.accountingBook;

import java.util.List;

import com.taoding.common.service.CrudService;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.accountingbook.Authorise;
import com.taoding.domain.accountingbook.TaxpayerProperty;
import com.taoding.mapper.accountingBook.AccountingBookDao;

 
public interface AccountingBookService extends CrudService<AccountingBookDao,AccountingBook>{
	
	/**
	 * 
	* @Description: TODO(账簿授权/指派) 
	* @param accountingIds 会计ID串
	* @param ccountingBookId 账薄ID
	* @param authoriseType 授权类型  0 指派 1授权
	* @param count 最大授权人数
	* @return int 返回类型    修改数量
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public int accountingBookAssignment(String accountingIds,String ccountingBookId,int authoriseType,int count );
	/**
	 * 
	* @Description:保存 Authorise
	* @param entity
	* @return int 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public int saveAuthorise(Authorise entity);
	
	
	/**
	 * 
	* @Description: TODO(根据条件获得会计制度 列表) 
	* @param accountSystem
	* @return List<AccountSystem> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public List<AccountSystem> findAccountSystemList(AccountSystem accountSystem);
	
	/**
	 * 
	* @Description: TODO(根据条件获得会计制度列表) 
	* @param accountSystem
	* @return List<AccountSystem> 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月20日
	 */
	public List<TaxpayerProperty> findTaxpayerPropertyList(TaxpayerProperty taxpayerProperty);
	
	/**
	 * 查询所有的账薄
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-20 16:47:27
	 */
	public List<AccountingBook> findAllList();
	
	
	/**
	 * 
	* @Description: TODO(save账薄) 
	* @param accountingBook void 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年12月5日
	 */
	public void saveAccountingBook(AccountingBook accountingBook);
	
	/**
	 * 获取做账区间
	 * 2017年12月11日 上午9:39:35
	 * @param bookId
	 * @return
	 */
	public Object getAccountingPeriod(String bookId);
}
