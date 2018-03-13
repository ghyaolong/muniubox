package com.taoding.service.settleaccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.settleaccount.CpaFinalLiquidationCustomer;
import com.taoding.domain.settleaccount.CpaFinalLiquidationTemplete;
import com.taoding.domain.user.User;
import com.taoding.mapper.settleaccount.CpaFinalLiquidationCustomerDao;
import com.taoding.service.accountingBook.AccountingBookService;

/**
 * 企业期末结转基础配置
 * @author czy
 * 2017年12月21日 下午3:40:39
 */
@Service
public class CpaFinalLiquidationCustomerServiceImpl implements
		CpaFinalLiquidationCustomerService {

	@Autowired
	private CpaFinalLiquidationCustomerDao finalLiquidationCustomerDao;
	@Autowired
	private CpaFinalLiquidationTempleteService finalLiquidationTempleteService;
	@Autowired
	private AccountingBookService accountingBookService;
	
	
	/**
	 * 初始化企业期末结转基础配置
	 * 2017年12月22日 上午10:19:24
	 * @param bookId
	 * @param customerId
	 * @return
	 */	
	@Override
	public int init(String bookId, String customerId) {
		
		AccountingBook accountBook = accountingBookService.get(bookId);
		if(accountBook == null || StringUtils.isEmpty(accountBook.getId())){
			throw new LogicException("获取不到当前账簿信息");
		}
		
		//纳税人性质
		String taxpayerProperty = accountBook.getTaxpayerPropertyId() ;
		
		List<CpaFinalLiquidationCustomer> lists = new ArrayList<CpaFinalLiquidationCustomer>();
		
		User createBy = new User();
		createBy.setId(UserUtils.getCurrentUserId());
		
		List<CpaFinalLiquidationTemplete> templeteLists = finalLiquidationTempleteService.findListByTaxpayerProperty(taxpayerProperty);
		if(templeteLists != null && templeteLists.size() > 0){
			for (CpaFinalLiquidationTemplete liquidationTemplete : templeteLists) {
				if(liquidationTemplete != null){
					CpaFinalLiquidationCustomer liquidationCustomer = new CpaFinalLiquidationCustomer();
					liquidationCustomer.setId(UUIDUtils.getUUid());
					liquidationCustomer.setBookId(bookId);
					liquidationCustomer.setCustomerId(customerId);
					liquidationCustomer.setSubKey(liquidationTemplete.getSubKey());
					liquidationCustomer.setSubName(liquidationTemplete.getSubName());
					liquidationCustomer.setEnable(true);
					liquidationCustomer.setCreateBy(createBy);
					liquidationCustomer.setCreateDate(new Date());
					lists.add(liquidationCustomer);
				}
			}
		}
		
		if(lists != null && lists.size() > 0){
			return finalLiquidationCustomerDao.batchInsert(lists);
		}
		return 0;
	}



	/**
	 * 通过账簿ID 获取 企业 期末结转配置
	 * 2017年12月21日 下午3:41:51
	 * @param bookId
	 * @return
	 */
	@Override
	public List<CpaFinalLiquidationCustomer> findListByBookId(String bookId) {
		return finalLiquidationCustomerDao.findListByBookId(bookId);
	}

}
