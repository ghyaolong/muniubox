package com.taoding.service.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.entity.DataEntity;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.initialize.InitEntity;
import com.taoding.domain.user.User;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.salary.CpaSalaryInfoService;
import com.taoding.service.salary.CpaSalaryWelfareSettingService;
import com.taoding.service.ticket.BankStatementService;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketListTemplateService;
import com.taoding.service.ticket.TicketService;
import com.taoding.service.user.UserService;

/**
 * 初始化Service
 * 
 * @author mhb
 * @version 2017-12-05
 */
@Service
public class InitializeDataServiceImpl implements
		InitializeDataService {
	@Autowired
	private UserService userService;
	@Autowired
	AccountingBookService accountingBookService;
	
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketListService ticketListService;
	@Autowired
	private TicketListTemplateService ticketListTemplateService;
	@Autowired
	private BankStatementService bankStatementService;
	@Autowired 
	private CpaSalaryWelfareSettingService  cpaSalaryWelfareSettingService; 
	@Autowired
	private CpaSalaryInfoService cpaSalaryInfoService;

	@Override
	@Transactional
	public Object init(InitEntity initEntity) {
		if (StringUtils.isBlank(initEntity.getLoginName()) || StringUtils.isBlank(initEntity.getPassWord())) {
			throw new LogicException("初始化操作用户名或密码为空!");
		} 
		if(StringUtils.isEmpty(initEntity.getAccountingBookId())){
			throw new LogicException("账薄信息为空");
		}
		String loginName = initEntity.getLoginName().trim();
		String passWord = initEntity.getPassWord().trim();
		String accountingBookId = initEntity.getAccountingBookId();
		if (!(loginName.equals(UserUtils.getCurrentLoginName()))) {
			throw new LogicException("当前登陆人与所要验证的用户名不符！");
		}
		if(!initEntity.isBookType()&&!initEntity.isPayType()&&!initEntity.isTicketType()){
			throw new LogicException("初始化项未选");
		}
		User user = userService.getUserByLoginName(loginName);
		if (user != null && StringUtils.isNotEmpty(user.getPassword())) {
			if (!UserUtils.validatePassword(passWord, user.getPassword())) {
				throw new LogicException("密码验证不通过.");
			}
			
			AccountingBook accountingBook = accountingBookService.get(accountingBookId);
			if (accountingBook == null) {
				throw new LogicException("所属账簿不存在！");
			}
			 // 账套数据初始化
			if (initEntity.isBookType()) {
				AccountingBook deleteAccountingBook = new AccountingBook();
				deleteAccountingBook.setId(accountingBookId);
				deleteAccountingBook.setDelFlag(DataEntity.DEL_FLAG_DELETE);
				accountingBookService.save(deleteAccountingBook);
				accountingBook.setId(null);
				accountingBookService.saveAccountingBook(accountingBook);
				return true;
			 // 票据数据初始化
			} else if (initEntity.isTicketType()) {
				ticketService.deleteAll(accountingBookId);    //删除票据 
				ticketListService.deleteAll(accountingBookId);  //删除目录
				ticketListTemplateService.deleteAll(accountingBookId);  //删除目录模板
				bankStatementService.deleteAll(accountingBookId);   //删除银行对账单
				ticketListService.init(accountingBook.getId());   //插入目录
			    ticketService.init(accountingBook.getId());       //账薄初始化
				ticketListTemplateService.init(accountingBook.getId()); 
				return true;
			}
			 // 薪酬管理数据初始化
			if (initEntity.isPayType()) {
				cpaSalaryWelfareSettingService.deleteProjectByCustomerId(accountingBook.getCustomerInfoId());//初始化社保公积金
				cpaSalaryWelfareSettingService.refreshWelfare(accountingBook.getCustomerInfoId()); //刷新客户社保公积金信息表
				cpaSalaryInfoService.deleteSalaryInfoByCustomerId(accountingBook.getCustomerInfoId());//初始化薪酬列表
				cpaSalaryInfoService.refreshSalaryInfo(accountingBook.getCustomerInfoId());//刷新薪酬列表 
				return true;
			}
		}
		throw new LogicException("初始化失败！");
	}

}
