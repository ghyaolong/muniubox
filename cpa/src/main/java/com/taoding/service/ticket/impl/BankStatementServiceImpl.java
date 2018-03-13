package com.taoding.service.ticket.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.domain.ticket.BankStatement;
import com.taoding.domain.ticket.BankStatementExample;
import com.taoding.domain.ticket.BankStatementExample.Criteria;
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.TicketExample;
import com.taoding.domain.ticket.vo.BankStatementVo;
import com.taoding.domain.ticket.vo.JsonToBankStatement;
import com.taoding.mapper.ticket.BankStatementDao;
import com.taoding.mapper.ticket.TicketDao;
import com.taoding.service.addition.CpaBankService;
import com.taoding.service.addition.CpaCustomerSubjectBankService;
import com.taoding.service.ticket.BankStatementService;
import com.taoding.service.ticket.TicketService;

@Service
@Transactional
public class BankStatementServiceImpl implements BankStatementService {

	@Autowired
	private BankStatementDao bankStatementDao;
	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private CpaBankService cpaBankService;
	@Autowired
	private CpaCustomerSubjectBankService cpaCustomerSubjectBankService;
	
	@Override
	public void init(String bookId) {}

	@Override
	public List<BankStatement> getBankStatements(String bookId, Date accountDate) {
		BankStatementExample example = new BankStatementExample();
		Criteria criteria = example.createCriteria();

		criteria.andBookIdEqualTo(bookId);
		criteria.andAccountDateEqualTo(accountDate);
		criteria.andDeletedEqualTo(FALSE);

		return bankStatementDao.selectByExample(example);
	}
	
	@Override
	public List<BankStatement> getBankStatements(String bookId, String bankId, Date accountDate) {
		BankStatementExample example = new BankStatementExample();
		Criteria criteria = example.createCriteria();

		criteria.andBookIdEqualTo(bookId);
		criteria.andBankIdEqualTo(bankId);
		criteria.andAccountDateEqualTo(accountDate);
		criteria.andDeletedEqualTo(FALSE);

		return bankStatementDao.selectByExample(example);
	}

	@Override
	public void insertBankStatement(Ticket ticket) throws LogicException {
		if (StringUtils.isBlank(ticket.getSummary())) {
			throw new LogicException("参数异常!");
		}

		List<JsonToBankStatement> statements = null;
		try {
			statements = JsonUtil.jsonToList(ticket.getSummary(), JsonToBankStatement.class);
		} catch (Exception e) {
			throw new LogicException("对账信息有误,解析失败!");
		}

		String bankName = ticket.getPayerName().contains("银行") ? ticket.getPayerName() : ticket.getPayeeName();
		CpaBank bank = cpaBankService.getBankList(bankName).get(0);
		Map<String, Object> condition = new HashMap<>();
		condition.put("customerId", ticket.getCustomerId());
		condition.put("bankId", bank.getId());
		CpaCustomerSubjectBank customerBank = cpaCustomerSubjectBankService.selectList(condition).get(0);

		int index = 1;
		for (JsonToBankStatement jsonToBankStatementn : statements) {
			BankStatement bankStatement = new BankStatement();
			bankStatement.setId(UUIDUtils.getUUid());
			bankStatement.setBookId(ticket.getBookId());
			bankStatement.setLineNo(String.format("%03d", index++));
			bankStatement.setTicketId(ticket.getId());
			bankStatement.setBankId(customerBank.getId());
			bankStatement.setAccountDate(ticket.getAccountDate());
			bankStatement.setStatementDate(DateUtils.parseDate(jsonToBankStatementn.getStatementDate()));
			bankStatement.setDirection(jsonToBankStatementn.getDirection());
			bankStatement.setAmount(jsonToBankStatementn.getAmount());
			bankStatement.setDeleted(FALSE);
			bankStatement.setCreated(ticket.getCreated());
			bankStatement.setUpdated(ticket.getUpdated());
			bankStatementDao.insertSelective(bankStatement);
		}
	}

	@Override
	public void ignoreStatement(String id) {
		BankStatement bankStatement = null;
		bankStatement = bankStatementDao.selectByPrimaryKey(id);
		if (bankStatement == null) {
			throw new LogicException("无此条信息!");
		}

		Date accountDate = bankStatement.getAccountDate();
		accountDate = DateUtils.getFirstDayOfMonth(accountDate, 1);

		bankStatement.setAccountDate(accountDate);
		bankStatement.setUpdated(new Date());
		bankStatementDao.updateByPrimaryKeySelective(bankStatement);
	}

	@Override
	public List<BankStatementVo> statement(String bookId, String bankId, Date accountDate) {
		List<BankStatementVo> result = new ArrayList<>();

		List<BankStatement> bankStatements = getBankStatements(bookId, bankId, accountDate);
		for (BankStatement bankStatement : bankStatements) {
			BankStatementVo vo = new BankStatementVo();
			vo.setStatementId(bankStatement.getId());
			vo.setStatementDate(bankStatement.getStatementDate());
			vo.setStatementIncome(
					bankStatement.getDirection() == TRUE ? bankStatement.getAmount() : new BigDecimal("0.00"));
			vo.setStatementCost(
					bankStatement.getDirection() == FALSE ? bankStatement.getAmount() : new BigDecimal("0.00"));

			TicketExample example = new TicketExample();
			example.setBookId(bookId);
			com.taoding.domain.ticket.TicketExample.Criteria criteria = example.createCriteria();
			criteria.andTypeEqualTo(TicketService.BANK_TICKET);
			criteria.andIsVoidEqualTo(FALSE);
			criteria.andIsLllegalEqualTo(FALSE);
			criteria.andTicketDateEqualTo(bankStatement.getStatementDate());
			List<Ticket> tickets = ticketDao.selectByExample(example);
			Ticket ticket = tickets.size() == 0 ? null : tickets.get(0);
			if (ticket == null || ticket.getTotalAmount().compareTo(bankStatement.getAmount()) != 0) {
				vo.setStatemented(Boolean.FALSE);
				result.add(vo);
				continue;
			}

			vo.setTicketId(ticket.getId());
			vo.setVoucherId(ticket.getVoucherId());
			vo.setTicketDate(ticket.getTicketDate());
			vo.setTicketIncome(
					bankStatement.getDirection() == TRUE ? bankStatement.getAmount() : new BigDecimal("0.00"));
			vo.setTicketCost(
					bankStatement.getDirection() == FALSE ? bankStatement.getAmount() : new BigDecimal("0.00"));
			vo.setStatemented(Boolean.TRUE);
			result.add(vo);
		}
		return result;
	}
	
	@Override
	public void deleteAll(String bookId) {
		BankStatement bankStatement = new BankStatement();
		bankStatement.setDeleted(TRUE);
		bankStatement.setUpdated(new Date());
		
		BankStatementExample example = new BankStatementExample();
		Criteria criteria = example.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		bankStatementDao.updateByExampleSelective(bankStatement, example);
	}

}
