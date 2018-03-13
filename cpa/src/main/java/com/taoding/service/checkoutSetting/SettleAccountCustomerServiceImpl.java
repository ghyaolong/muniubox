package com.taoding.service.checkoutSetting;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.checkoutSetting.SettleAccountBasic;
import com.taoding.domain.checkoutSetting.SettleAccountCustomer;
import com.taoding.mapper.checkoutSetting.SettleAccountBasicDao;
import com.taoding.mapper.checkoutSetting.SettleAccountCustomerDao;
import com.taoding.mapper.checkoutSetting.SettleAccountOperatingSettingDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.customerInfo.CustomerInfoService;
/**
 * 企业结账启用禁用配置ServiceImpl
 * 
 * @author mhb
 * @version 2017-12-25
 */
@Slf4j
@Service
public class SettleAccountCustomerServiceImpl extends DefaultCurdServiceImpl<SettleAccountCustomerDao, SettleAccountCustomer> implements
SettleAccountCustomerService {
    @Autowired
    private CustomerInfoService customerInfoService;
	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private SettleAccountBasicDao settleAccountBasicDao;
	@Autowired
	private SettleAccountOperatingSettingDao settleAccountOperatingSettingDao;
	@Override
	@Transactional
	public List<SettleAccountCustomer> findGeneralTypeValidationListData(String bookId){
		if(accountingBookService.get(bookId)==null){
			throw new LogicException("账薄或信息不存在");
		}
		return dao.findList(bookId,SettleAccountCustomer.VALIDATION_TYPE_GENERAL); 
	}
	
	@Override
	@Transactional
	public List<SettleAccountCustomer> findOtherTypeValidationListData(String bookId){
		if(accountingBookService.get(bookId)==null){
			throw new LogicException("账薄或信息不存在");
		}
		return dao.findList(bookId,SettleAccountCustomer.VALIDATION_TYPE_OTHER); 
	}
	
	@Override
	@Transactional
	public boolean initSave(String bookId,String customerId){
		if(accountingBookService.get(bookId)==null||customerInfoService.get(customerId)==null){
			throw new LogicException("账薄或客户信息不存在");
		}
		log.info("bookId:"+bookId+";customerId"+customerId);
		List<SettleAccountBasic> settleAccountBasicList= settleAccountBasicDao.findAllList();
		List<SettleAccountCustomer> settleAccountCustomerList= new LinkedList<SettleAccountCustomer>();
		for (SettleAccountBasic settleAccountBasic : settleAccountBasicList) {
			SettleAccountCustomer settleAccountCustomer = new SettleAccountCustomer();
			BeanUtils.copyProperties(settleAccountBasic,settleAccountCustomer);
			settleAccountCustomer.setBookId(bookId);
			settleAccountCustomer.setCustomerId(customerId);
			settleAccountCustomer.setEnable(SettleAccountCustomer.TRUE);
			settleAccountCustomer.setId(UUIDUtils.getUUid());
			this.preInsert(settleAccountCustomer);
			settleAccountCustomerList.add(settleAccountCustomer);
		}
		  log.info("settleAccountCustomerList"+JSON.toJSONString(settleAccountCustomerList));
		  dao.batchInsert(settleAccountCustomerList); 
		  return true;
	}
	@Override
	public boolean updateEnabled(String id) {
		if(StringUtils.isEmpty(id)||dao.get(id)==null){
			throw new LogicException("启用/禁用信息不存在!");
		}
		boolean flag= false;
		if(!dao.get(id).isEnable()){
			flag=SettleAccountCustomer.TRUE;
		}
		dao.updateEnabled(id,flag);
		return true;
	}
}
