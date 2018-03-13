package com.taoding.service.accountingBook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.DateUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.accountingbook.AccountSystem;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.accountingbook.Authorise;
import com.taoding.domain.accountingbook.TaxpayerProperty;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.mapper.accountingBook.AccountingBookDao;
import com.taoding.service.customerInfo.CustomerInfoService;
import com.taoding.service.customerTaxItem.CustomerTaxItemService;
import com.taoding.service.fixedAsset.FixedAssetService;
import com.taoding.service.subject.CpaCustomerSubjectService;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketListTemplateService;
import com.taoding.service.ticket.TicketService;
import com.taoding.service.voucher.CpaVoucherService;
import com.taoding.service.voucher.CpaVoucherSubjectService;

@Service
@Transactional(readOnly=true)
public class AccountingBookServiceImp extends
		DefaultCurdServiceImpl<AccountingBookDao, AccountingBook> implements
		AccountingBookService {

	@Autowired
	private CpaCustomerSubjectService customerSubjectService ;
	@Autowired
	private CpaVoucherService voucherService;
	@Autowired
	private CpaVoucherSubjectService voucherSubjectService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketListService ticketListService;
	@Autowired
	private TicketListTemplateService ticketListTemplateService;
	@Autowired
	private CustomerInfoService customerInfoService;
	@Autowired
	private CustomerTaxItemService customerTaxItemService;
	
	@Autowired
	private FixedAssetService  fixedAssetService;
	
	@Override
	@Transactional(readOnly=false)
	public int accountingBookAssignment(String accountingIds,
			String accountingBookId, int authoriseType, int count) {

		if (StringUtils.isBlank(accountingIds)
				|| StringUtils.isBlank(accountingBookId)) {
			return -1;
		}
		if (Authorise.AUTHORISE_TYPE != authoriseType
				&& Authorise.AUTHORISE_TYPE_ASSIGN != authoriseType)
			return -1;
		
		AccountingBook accountingBook = dao.get(accountingBookId);
		
		if(null == accountingBook){
			throw new LogicException("所属账簿不存在！");
		}
		
		int updateCount = 0;
		Authorise entity = null;
		
		String[] accountingIdArray = accountingIds.split(",");
		entity = new Authorise();
		entity.setAccountingBookId(accountingBookId);
		entity.setAuthoriseType(authoriseType);
		List<Authorise> list = dao.selectAuthoriseList(entity);

		if (Authorise.AUTHORISE_TYPE_ASSIGN == authoriseType) {// 指派
			String accountingId = accountingIdArray[0];
			if (CollectionUtils.isEmpty(list)) {
				entity.setAccountingId(accountingId);
				saveAuthorise(entity);
				accountingBook.setAssignedStatus(Global.YES_INT);
				save(accountingBook);
			} else {
				entity = list.get(0);
				if (StringUtils.isNotBlank(entity.getAccountingId())
						&& !entity.getAccountingId().equals(accountingId)) {
					entity.setAccountingId(accountingId);
					saveAuthorise(entity);
					accountingBook.setAssignedStatus(Global.YES_INT);
					save(accountingBook);
				}
			}

		} else if (Authorise.AUTHORISE_TYPE == authoriseType) {// 授权

			if(accountingIdArray.length>count){//超出授权数量
				throw new LogicException("授权数量不能超过"+count+"个");
			}
			
			
			if (!CollectionUtils.isEmpty(list)) {
				int index = ArrayUtils.INDEX_NOT_FOUND;
				for (Authorise a : list) {
					index = ArrayUtils.indexOf(accountingIdArray,
							a.getAccountingId());
					if (index != ArrayUtils.INDEX_NOT_FOUND) {
						accountingIdArray=ArrayUtils.remove(accountingIdArray, index);
					} else {
						dao.deleteAuthorise(a.getId());
					}
				}

			}
			if (ArrayUtils.isNotEmpty(accountingIdArray)) {
				for (int i = 0; i < accountingIdArray.length; i++) {
					if(StringUtils.isBlank(accountingIdArray[i])) continue;
					entity= new Authorise();
					entity.setAccountingBookId(accountingBookId);
					entity.setAuthoriseType(authoriseType);
					entity.setAccountingId(accountingIdArray[i]);
					updateCount++;
					saveAuthorise(entity);
				}
			}
			accountingBook.setAuthoriseStatus(Global.YES_INT);
			save(accountingBook);
		}
		return updateCount;
	}


	@Override
	@Transactional(readOnly=false)
	public int saveAuthorise(Authorise entity) {

		int flag =-1;
		if (entity.getIsNewRecord()) {
			entity.setId(UUID.randomUUID().toString().replace("-", ""));
			this.preInsert(entity);
			flag=dao.insertAuthorise(entity);

		} else {
			this.preUpdate(entity);
			flag=dao.updateAuthorise(entity);
		}

		return flag;
	}

	@Override
	public List<AccountSystem> findAccountSystemList(AccountSystem accountSystem) {
		return dao.findAccountSystemList(accountSystem);
	}

	@Override
	public List<TaxpayerProperty> findTaxpayerPropertyList(
			TaxpayerProperty taxpayerProperty) {
		return dao.findTaxpayerPropertyList(taxpayerProperty);
	}


	/**
	 * 获取所有的账薄
	 */
	public List<AccountingBook> findAllList() {
		return dao.findAllList();
	}

	@Override
	@Transactional(readOnly=false)
	public void saveAccountingBook(AccountingBook accountingBook) {
		
		if(null != accountingBook && StringUtils.isBlank(accountingBook.getId())&&StringUtils.isNotEmpty(accountingBook.getCustomerInfoId())){
			//添加时更改当前账期为初始账套
			CustomerInfo customerInfo= customerInfoService.get(accountingBook.getCustomerInfoId());
			//同步公司信息表中的纳税人性质给账薄表
			if(customerInfo!=null&&StringUtils.isNotEmpty(customerInfo.getType())){
				accountingBook.setTaxpayerPropertyId(customerInfo.getType());
			}
			accountingBook.setCurrentPeriod(accountingBook.getAccountingStartDate());
			accountingBook.setAccountStatus(AccountingBook.ACCOUNT_STATUS_ALREADY);
			save(accountingBook);
			if(StringUtils.isNotBlank(accountingBook.getId()) && StringUtils.isNotBlank(accountingBook.getCustomerInfoId())){
				
				
				//初始化 凭证
				voucherService.init(accountingBook.getId());
				voucherSubjectService.init(accountingBook.getId()); 
				//初始化企业基础科目
				customerSubjectService.init(accountingBook.getId(), accountingBook.getCustomerInfoId());
				
				//初始化票据
				ticketListService.init(accountingBook.getId());
			    ticketService.init(accountingBook.getId());
				ticketListTemplateService.init(accountingBook.getId()); 
				
				//初始化税项模板
				customerTaxItemService.saveCustomerTaxtemplate(accountingBook);
				
				//初始化资产类型
				fixedAssetService.initAssetTypeFromAssetTypeTemplateBybookId(accountingBook.getId());
				
			}
		}else{
			save(accountingBook);
		}
	}


	/**
	 * 获取做账区间
	 * 2017年12月11日 上午9:39:35
	 * @param bookId
	 * @return
	 */
	@Override
	public Object getAccountingPeriod(String bookId) {
		AccountingBook accountingBook =  dao.get(bookId);
		if(accountingBook != null && StringUtils.isNotEmpty(accountingBook.getId())){
			Map<String,String> maps = new HashMap<String,String>();
			String voucherPeriod = "";
			if(accountingBook.getAccountStatus() == 5){ //5已完成 获取下个账期数据
				//获取当前账期的下月第一天
				voucherPeriod = DateUtils.getPerFirstDayOfMonth(accountingBook.getCurrentPeriod());
			}else{
				//获取当前账期的当月第一天
				voucherPeriod = DateUtils.getFirstDayOfMonth(accountingBook.getCurrentPeriod());
			}
			maps.put("startPeriod", DateUtils.getFirstDayOfMonth(accountingBook.getAccountingStartDate()));
			maps.put("currentPeriod", voucherPeriod);
			return maps ;
		}
		return new LogicException("查询不到账簿信息");
	}
	
	
	
	
}
