package com.taoding.service.ticket.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.TicketExample;
import com.taoding.domain.ticket.TicketExample.Criteria;
import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.TicketListTemplate;
import com.taoding.domain.ticket.vo.Condition;
import com.taoding.domain.ticket.vo.Template;
import com.taoding.mapper.ticket.TicketDao;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.customerInfo.CustomerInfoService;
import com.taoding.service.ticket.BankStatementService;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketListTemplateService;
import com.taoding.service.ticket.TicketService;

/**
 * 票据业务接口
 * 
 * @author 刘鑫
 * @category 票据业务接口
 *
 */
@Service
@Transactional
public class TicketServiceImpl implements TicketService {
	
	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private TicketListService ticketListService;
	@Autowired
	private TicketListTemplateService ticketListTemplateService;
	@Autowired
	private BankStatementService bankStatementService;

	@Autowired
	private AccountingBookService accountingBookService;
	@Autowired
	private CustomerInfoService customerInfoService;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void init(String bookId) {
		ticketDao.init(bookId);
	}

	@Override
	public Ticket geTicketById(String bookId, String id) {
		if (getAccountingBook(bookId) == null) {
			throw new LogicException("查无此账簿!");
		}
		return ticketDao.selectByPrimaryKey(bookId, id);
	}

	@Override
	public List<Ticket> getTicketsByListId(String bookId, String listId, Date date, Condition condition) {
		if (StringUtils.isBlank("bookId") || StringUtils.isBlank(listId)) {
			// throw new LogicException("账簿ID 或 目录ID 为空!");
			return null;
		}

		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andListIdEqualTo(listId);
		criteria.andAccountDateEqualTo(date);
		criteria.andDeletedEqualTo(FALSE);
		if (condition.getIsVoid() != null) {
			criteria.andIsVoidEqualTo(condition.getIsVoid());
		}
		if (condition.getIsLllegal() != null) {
			criteria.andIsLllegalEqualTo(condition.getIsLllegal());
		}
		if (condition.getCompleted() != null) {
			criteria.andCompletedEqualTo(condition.getCompleted());
		}
		if (condition.getIsIdentify() != null) {
			criteria.andIsIdentifyEqualTo(condition.getIsIdentify());
		}

		return ticketDao.selectByExample(example);
	}

	@Override
	public List<Ticket> getTickets(Map<String, Object> condition) {
		if (condition == null) {
			return null;
		}
		return ticketDao.selectList(condition);
	}

	@Override
	public List<Ticket> getTicketsBySummaryNotInList(String bookId, String summaryWord, String listId) {
		if (StringUtils.isBlank(bookId) || StringUtils.isBlank(summaryWord) || StringUtils.isBlank(listId)) {
			return null;
		}

		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andSummaryWordEqualTo(summaryWord); // 查询条件
		criteria.andListIdNotEqualTo(listId); // 查询条件
		criteria.andIsVoidEqualTo(FALSE);
		criteria.andCompletedEqualTo(Byte.valueOf(FALSE));
		criteria.andDeletedEqualTo(FALSE);
		return ticketDao.selectByExample(example);
	}
	
	@Override
	public long countByAccountDate(String bookId, Date accountDate) {
		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andAccountDateEqualTo(accountDate);
		criteria.andDeletedEqualTo(FALSE);
		return ticketDao.countByExample(example);
	}

	@Override
	public void insertTicket(Ticket ticket) {
		if (ticket == null 
				|| ticket.getBookId() == null 
				|| ticket.getCustomerId() == null
				|| getCustomerInfo(ticket.getCustomerId()) == null 
				|| getAccountingBook(ticket.getBookId()) == null) {
			throw new LogicException("参数异常!");
		}

		Date date = new Date();
		ticket.setId(UUIDUtils.getUUid());
		// ticket.setAccountingId(UserUtils.getCurrentUserId());
		ticket.setAccountingId("");
		ticket.setAccountDate(DateUtils.getFirstDayOfMonth()); // 设置当前账期
		ticket.setTicketNo(generateTicketNo(ticket.getBookId(), ticket.getAccountDate())); // 需要生成规则
		ticket.setCreated(date);
		ticket.setUpdated(date);
		
		// 如果票据未识别
		if (isUnidentified(ticket)) {
			TicketList ticketList = getUnidentifiedTicketList(ticket.getBookId());
			ticket.setListId(ticketList.getId());
			ticket.setListIds(ticketList.getParentIds());	
			ticket.setIsIdentify(FALSE);
			ticketDao.insertSelective(ticket);
			return;
		}

		// 票据是否存在错误
		if (isErrorTicket(ticket)) {
			TicketList ticketList = getLllegalTicketList(ticket.getBookId());
			ticket.setListId(ticketList.getId());
			ticket.setListIds(ticketList.getParentIds());
			ticketDao.insertSelective(ticket);
			return;
		}

		// 检查此票据摘要是否在黑名单中
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(ticket.getBookId());
		String blacklist = ticketListTemplate.getSummaryBlacklist();
		List<String> summaryBlacklist = StringUtils.isBlank(blacklist) ? null
				: JsonUtil.jsonToList(blacklist, String.class);
		if (summaryBlacklist != null) {
			for (String list : summaryBlacklist) {
				if (ticket.getSummaryWord().equals(list)) {
					ticket.setIsVoid(TRUE);
					ticketDao.insert(ticket);
					return;
				}
			}
		}

		// 根据不同的票据类型设置 票据所属的目录ID 和目录IDS
		List<Template> templates = JsonUtil.jsonToList(ticketListTemplate.getTemplate(), Template.class);
		Template target = targetTemplate(ticket, templates);
		
		TicketList ticketList;
		if (target != null) {
			ticketList = ticketListService.getTicketListById(target.getListId());
		} else {// 没有找到对应关系，存入预设目录
			ticketList = defaultTicketList(ticket, ticket.getType());
		}
		
		// 设置票据的目录归属
		setTicketList(ticket, ticketList, ticketListTemplate.getIsRestByTax());

		if (ticket.getType() == BANK_LIST_TICKET) {// 如果是银行对账单，建立相应的对账单数据
			bankStatementService.insertBankStatement(ticket);
		}

		// 持久化
		ticketDao.insertSelective(ticket);
	}

	@Override
	public void insertTickets(Ticket[] tickets) {
		for (Ticket ticket : tickets) {
			insertTicket(ticket);
		}
	}

	@Override
	public void insertTickets(List<Ticket> tickets) {
		for (Ticket ticket : tickets) {
			insertTicket(ticket);
		}
	}

	@Override
	public void updateTicket(Ticket ticket) {
//		Ticket ticketDB = geTicketById(ticket.getBookId(), ticket.getId());

		if (StringUtils.isNotBlank(ticket.getSummaryWord())) {// 如果摘要属性发生修改

			// 获得模板
			TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(ticket.getBookId());
			Template template = targetTemplate(ticket,
					JsonUtil.jsonToList(ticketListTemplate.getTemplate(), Template.class));

			if (template == null) {// 如果此对应关系不存在，则更新票据目录至 费用-->>其他 目录下
				TicketList ticketList = defaultTicketList(ticket, ticket.getType());
				ticket.setListId(ticketList.getId());
				ticket.setListIds(ticketList.getParentIds());
				ticket.setSubjectContent(ticketList.getSubjectContent());
			} else {// 如果此对应关系存在 ， 则更新为现有的关系
				ticket.setListId(template.getListId());
				ticket.setListIds(template.getListIds());
				TicketList ticketList = ticketListService.getTicketListById(template.getListId());
				ticket.setSubjectContent(ticketList.getSubjectContent());
			}

		}
		
//		if (!isErrorTicket(ticket)) {
//			ticket.setIsLllegal(FALSE);
//			ticket.setLllegalExplain(null);
//		}

		ticket.setUpdated(new Date());
		ticketDao.updateByPrimaryKeySelective(ticket);
	}

	@Override
	public void moveTicketToList(String bookId, String id, String listId) {
		if (StringUtils.isBlank(bookId) || StringUtils.isBlank(id) || StringUtils.isBlank(listId)) {
			throw new LogicException("参数异常!");
		}
		Ticket ticket = geTicketById(bookId, id);
		TicketList ticketList = ticketListService.getTicketListById(listId);
		
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);

		// 移动目录修改数据
		moveList(ticket, ticketList, ticketListTemplate.getIsRestByTax());
		// 判断是否需要修改模板规则
		updateTemplating(ticket, ticketList);
	}

	@Override
	public void moveTicketsToList(String bookId, String[] ids, String listId) {
		if (StringUtils.isBlank(bookId) || ids == null || ids.length == 0 || StringUtils.isBlank(listId)) {
			throw new LogicException("参数异常!");
		}
		List<Ticket> tickets = null;
		TicketList ticketList = null;

		// 查询所有需要移动的票据
		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andIdIn(new ArrayList<>(Arrays.asList(ids)));
		tickets = ticketDao.selectByExample(example);

		// 查询移动目标目录
		ticketList = ticketListService.getTicketListById(listId);
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		// 批量移动到目标目录
		moveList(tickets, ticketList, ticketListTemplate.getIsRestByTax());
		// 批量修改目录：摘要模板
		updateTemplating(tickets, ticketList);
	}
	
	@Override
	public void moveTicketsToList(String bookId, String resource, String listId, Date accountDate) {
		if (StringUtils.isBlank(bookId)
				|| StringUtils.isBlank(resource)
				|| StringUtils.isBlank(listId)
				|| accountDate == null
				|| resource.equals(listId)) {
			throw new LogicException("参数异常!");
		}
		Condition condition = new Condition(false, false, null, null);
		List<Ticket> tickets = getTicketsByListId(bookId, resource, accountDate, condition);
		
		TicketList ticketList = ticketListService.getTicketListById(listId);
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		
		moveList(tickets, ticketList, ticketListTemplate.getIsRestByTax());
		updateTemplating(tickets, ticketList);
	}

	@Override
	public void reset(String bookId, Date date) {
		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andAccountDateEqualTo(date);
		criteria.andIsVoidEqualTo(FALSE);
		criteria.andCompletedEqualTo(FALSE);
		criteria.andDeletedEqualTo(FALSE);
		List<Ticket> tickets = ticketDao.selectByExample(example);

		if (tickets == null || tickets.size() == 0) {
			return;
		}

		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		reset(tickets, ticketListTemplate);
	}

	@Override
	public void resetByList(String bookId, String listId, Date date) {
		// 查询指定目录下所有票据
		Condition condition = new Condition(false, false, null, null);
		List<Ticket> tickets = getTicketsByListId(bookId, listId, date, condition);
		if (tickets == null || tickets.size() == 0) {
			return;
		}

		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		reset(tickets, ticketListTemplate);
	}
	
	@Override 
	public void deleteAll(String bookId) {
		Ticket ticket = new Ticket();
		ticket.setUpdated(new Date());
		ticket.setBookId(bookId);
		ticket.setDeleted(TRUE);
		TicketExample example = new TicketExample();
		ticketDao.updateByExampleSelective(ticket, example);
	}
	
	@Override
	public boolean hasUnComplatedTicket(String bookId) {
		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andCompletedEqualTo(FALSE);
		criteria.andIsLllegalEqualTo(FALSE);
		criteria.andIsVoidEqualTo(FALSE);
		criteria.andIsIdentifyEqualTo(TRUE);
		criteria.andDeletedEqualTo(FALSE);
		List<Ticket> tickets = ticketDao.selectByExample(example);
		return tickets != null && tickets.size() != 0;
	}

	/*
	 * -------------------------------------------私有方法------------------------------
	 * ------------------
	 */

	/**
	 * <pre>
	 * 票据的收款方或者付款方名称与账簿主体名称不相符
	 * </pre>
	 * 
	 * <pre>
	 * 票据的日期不能再账期之后
	 * </pre>
	 */
	private boolean isErrorTicket(Ticket ticket) {
		CustomerInfo customerInfo = getCustomerInfo(ticket.getCustomerId());
		if (!customerInfo.getName().equals(ticket.getPayerName())
				&& !customerInfo.getName().equals(ticket.getPayeeName())) {
			ticket.setIsLllegal(TRUE);
			ticket.setLllegalExplain("收款方或付款付与公司名称不一致");
			return true;
		}
		if (ticket.getTicketDate().getTime() > DateUtils.getLastDayOfMonth(ticket.getAccountDate()).getTime()) {
			ticket.setIsLllegal(TRUE);
			ticket.setLllegalExplain("开票的日期超出当前账期!");
			return true;
		}
		return false;
	}

	/**
	 * 票据是否被识别(关键信息)
	 * @param ticket
	 * @return
	 */
	private boolean isUnidentified(Ticket ticket) {
//		if (ticket.getIsIdentify() == null || ticket.getIsIdentify() == FALSE) {
//			return true;
//		}
		if (ticket.getType() == null) {
			return true;
		}
		if(StringUtils.isBlank(ticket.getSummaryWord()) || ticket.getTotalAmount() == null) {
			return true;
		}
		return false;
	}

	/**
	 * 从模板集合中获取票据所属的目标目录模板
	 * @param ticket
	 * @param templates
	 * @return
	 */
	private Template targetTemplate(Ticket ticket, List<Template> templates) {
		Template target = null;
		switch (ticket.getType()) {
		case BANK_TICKET:
			for (Template template : templates) {
				if (template.isBankTicket()) {
					if (template.getSummary().equals(ticket.getSummaryWord())) {
						target = template;
						break;
					}
				}
			}
			break;
		case BANK_LIST_TICKET:
			for (Template template : templates) {
				if (template.isAccountStatement()) {
					if (template.getSummary().equals(ticket.getSummaryWord())) {
						target = template;
						break;
					}
				}
			}
			break;
		case CERTIFY_TICKET:
			for (Template template : templates) {
				if (template.isCertify()) {
					if (template.getSummary().equals(ticket.getSummaryWord())) {
						target = template;
						break;
					}
				}
			}
			break;
		default:
			for (Template template : templates) {
				if (!template.isBankTicket() && !template.isAccountStatement() && !template.isCertify()) {
					if (template.getSummary().equals(ticket.getSummaryWord())) {
						target = template;
						break;
					}
				}
			}
			break;
		}

		return target;

	}

	/**
	 * 移动目录
	 */
	private void moveList(Ticket ticket, TicketList ticketList, byte isRestByRax) {
		if (ticket == null || ticketList == null) {
			throw new LogicException("票据或目录参数有误!");
		}
		
		setTicketList(ticket, ticketList, isRestByRax);
		ticket.setUpdated(new Date());
		ticketDao.updateByPrimaryKey(ticket);
	}

	/**
	 * 批量移动目录
	 * 
	 * @param tickets
	 */
	private void moveList(List<Ticket> tickets, TicketList ticketList, byte isRestByRax) {
		for (Ticket ticket : tickets) {
			moveList(ticket, ticketList, isRestByRax);
		}
	}

	/**
	 * 修改临时模板
	 * 
	 * @param ticket
	 */
	private void updateTemplating(Ticket ticket, TicketList target) {
		
		if (ticket.getIsIdentify() == FALSE 
				|| ticket.getIsLllegal() == TRUE
				|| ticket.getIsVoid() == TRUE) {
			return;
		}

		List<Ticket> tickets = getTicketsBySummaryNotInList(ticket.getBookId(), ticket.getSummaryWord(),
				target.getId());

		if (tickets == null || tickets.size() == 0) {// 如果其他目录都没有此票据，则修改模板数据
			TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(ticket.getBookId()); // 获得模板
			List<Template> templates = JsonUtil.jsonToList(ticketListTemplate.getTemplating(), Template.class);

			Template template = targetTemplate(ticket, templates);

			template.setListId(ticket.getListId());
			template.setListIds(ticket.getListIds());

			if (target.getName().equals(TicketListService.BANK_LIST)) {
				template.setBankTicket(true);
			} else if (target.getName().equals(TicketListService.BANK_LIST_LIST)) {
				template.setAccountStatement(true);
			} else if (target.getName().equals(TicketListService.CERTIFY_LIST)) {
				template.setCertify(true);
			}

			ticketListTemplate.setTemplating(JsonUtil.objectToJson(templates));
			ticketListTemplate.setUpdated(new Date());
			ticketListTemplateService.updateTemplate(ticketListTemplate);
		}
	}

	/**
	 * 批量修改临时模板
	 * 
	 * @param tickets
	 */
	private void updateTemplating(List<Ticket> tickets, TicketList target) {
		if (tickets == null || tickets.size() == 0) {
			return;
		}

		Ticket ticket = tickets.remove(0);
		for (Ticket t : new ArrayList<>(tickets)) {
			if (ticket.getSummaryWord().equals(t.getSummaryWord())) {
				tickets.remove(t);
			}
		}

		updateTemplating(ticket, target); // 修改模板
		updateTemplating(tickets, target); // 重载此方法
	}

	/**
	 * 重置票据所属的目录
	 * <pre>1.未识别票据归类</pre>
	 * <pre>2.问题票据票据归类</pre>
	 * <pre>3.属于黑名单摘要的票据设置</pre>
	 * <pre>4.按照模板归类</pre>
	 * <pre>5.未找到模板的归类至预设目录</pre>
	 * @param tickets
	 */
	private void reset(List<Ticket> tickets, TicketListTemplate ticketListTemplate) {
		
		TicketList unIdentifyList = ticketListService.getTicketListByName(ticketListTemplate.getBookId(), TicketListService.UNIDENTIFY_LIST);
		TicketList lllegalList = ticketListService.getTicketListByName(ticketListTemplate.getBookId(), TicketListService.LLLEGAL_LIST);
		
		Date date = new Date();
		List<Template> templates = JsonUtil.jsonToList(ticketListTemplate.getTemplate(), Template.class);
		for (Ticket ticket : tickets) {
			// 未识别票据归类
			if (ticket.getIsIdentify() == FALSE) {
				ticket.setListId(unIdentifyList.getId());
				ticket.setListIds(unIdentifyList.getParentIds());
				continue;
			}
			
			// 问题票据归类
			if (ticket.getIsLllegal() == TRUE) {
				ticket.setListId(lllegalList.getId());
				ticket.setListIds(lllegalList.getParentIds());
			}
			
			// 摘要黑名单设置作废
			String blacklist = ticketListTemplate.getSummaryBlacklist();
			if (StringUtils.isNotBlank(blacklist)) {
				for (String summary : JsonUtil.jsonToList(blacklist, String.class)) {
					if (ticket.getSummaryWord().equals(summary)) {
						ticket.setIsVoid(TRUE);
					}
				}
			}
			
			TicketList ticketList = null;
			Template template = targetTemplate(ticket, templates);
			if (template == null) { // 设置为预设的目录
				ticketList = defaultTicketList(ticket, ticket.getType());
			} else {
				ticketList = ticketListService.getTicketListById(template.getListId());
			}
			setTicketList(ticket, ticketList, ticketListTemplate.getIsRestByTax());
			ticket.setUpdated(date);
			ticketDao.updateByPrimaryKey(ticket);
		}
	}

	private TicketList defaultTicketList(Ticket ticket, byte type) {
		TicketList ticketList = null;

		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", ticket.getBookId());
		condition.put("deleted", 0);

		boolean isFieldCost = false;

		switch (type) {
		case BANK_TICKET:
			condition.put("name", TicketListService.BANK_LIST);
			break;
		case BANK_LIST_TICKET:
			condition.put("name", TicketListService.BANK_LIST_LIST);
			break;
		case CERTIFY_TICKET:
			condition.put("name", TicketListService.CERTIFY_LIST);
			break;
		default:
			// 收款方是否为代帐公司
			CustomerInfo customerInfo = getCustomerInfo(ticket.getCustomerId());

			if (ticket.getPayeeName().equals(customerInfo.getName())) {// 如果属于收入
				condition.put("name", TicketListService.INCOME_LIST);
			} else {// 如果属于费用
				condition.put("name", TicketListService.COST_LIST);
				if (!customerInfo.getProvince().contains(ticket.getProvince())
						&& !customerInfo.getCity().contains(ticket.getCity())) {// 是否属于外地费用
					isFieldCost = true;
				}
				String summaryWord = ticket.getSummaryWord();
				if ("飞机票".contains(summaryWord) || "火车票".contains(summaryWord)) {
					isFieldCost = true;
				}
			}

			break;
		}

		ticketList = ticketListService.getTicketLists(condition).get(0);
		List<TicketList> ticketLists = ticketListService.getTicketListsByParentId(ticket.getId(), ticket.getBookId());
		if (isFieldCost) {
			for (TicketList t : ticketLists) {
				if (t.getName().equals("差旅费")) {
					ticketList = t;
					break;
				}
			}
		} else {
			if (ticketLists != null && ticketLists.size() != 0) {
				for (TicketList t : ticketLists) {
					if (t.getIsDefault().intValue() == 1) {
						ticketList = t;
						break;
					}
				}
			}
		}

		return ticketList;
	}
	
	private String generateTicketNo(String bookId, Date date) {
		PageHelper.startPage(1, 1);
		PageHelper.orderBy("ticket_no desc");
		TicketExample example = new TicketExample();
		example.setBookId(bookId);
		Criteria criteria = example.createCriteria();
		criteria.andAccountDateEqualTo(date);
		criteria.andDeletedEqualTo(FALSE);
		List<Ticket> tickets = ticketDao.selectByExample(example);

		if (tickets == null || tickets.size() == 0) {
			return "0001";
		}
		String ticketNo = tickets.get(0).getTicketNo();
		int no = Integer.parseInt(ticketNo) + 1;

		return String.format("%04d", no);
	}

	@Cacheable(value = "customerInfo")
	private CustomerInfo getCustomerInfo(String customerId) {
		return customerInfoService.get(customerId);
	}

	@Cacheable(value = "accountingBook")
	private AccountingBook getAccountingBook(String bookId) {
		return accountingBookService.get(bookId);
	}

	/**
	 * 获取问题票据的目录
	 * 
	 * @param bookId
	 * @return
	 */
	@Cacheable(value = "ticketList")
	private TicketList getLllegalTicketList(String bookId) {
		return ticketListService.getTicketListByName(bookId, TicketListService.LLLEGAL_LIST);
	}
	
	/**
	 * 获得未识别票据所属的目录
	 * @param bookId
	 * @return
	 */
	@Cacheable(value="ticketList")
	private TicketList getUnidentifiedTicketList(String bookId) {
		return ticketListService.getTicketListByName(bookId, TicketListService.UNIDENTIFY_LIST);
	}
	
	/**
	 * 设置票据的目录归属
	 * @param ticket
	 * @param target
	 * @param isRestByTax
	 */
	private void setTicketList(Ticket ticket, TicketList target, byte isRestByTax) {
		List<String> parentIds = JsonUtil.jsonToList(target.getParentIds(), String.class);
		TicketList ticketListTop = ticketListService.getTicketListById(parentIds.get(1));
		
		if (isRestByTax == TRUE 
				&& (ticketListTop.getName().equals(TicketListService.INCOME_LIST) 
						|| ticketListTop.getName().equals(TicketListService.STOCK_LIST))
				&& ticketListService.getTicketListLevel(target) >= 2) {// 如果按照税率分类
			BigDecimal taxRate = ticket.getTaxRate();
			String ticketListName = taxRate == null ? "其他税率" :
										String.format("%%%.2s", taxRate.multiply(new BigDecimal("100")));
			
			TicketList ticketList = null;
			List<TicketList> ticketLists = ticketListService.getTicketListsByParentId(target.getId(), target.getBookId());
			if (ticketLists != null && ticketLists.size() != 0) {
				for (TicketList list : ticketLists) {
					if (list.getName().equals(ticketListName)) {
						ticketList = list;
						break;
					}
				}
			}
			
			if (ticketList == null) {
				ticketList = new TicketList();
				ticketList.setName(ticketListName);
				ticketList.setBookId(target.getBookId());
				ticketList.setParentId(target.getId());
				ticketListService.insertTicketList(ticketList);
			}
			
			ticket.setListId(ticketList.getId());
			ticket.setListIds(ticketList.getParentIds());
			ticket.setSubjectContent(ticketList.getSubjectContent());
			
		} else {
			ticket.setListId(target.getId());
			ticket.setListIds(target.getParentIds());
			ticket.setSubjectContent(target.getSubjectContent());
		}
	}
}
