package com.taoding.controller.ticket;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.service.ticket.BankStatementService;

@RestController
public class BankStatementController extends BaseController {
	
	@Autowired
	private BankStatementService bankStatementService;

	/**
	 * 对账操作
	 * @param bookId
	 * @param accountDate
	 * @return
	 */
	@GetMapping("/bank/statement/{bookId}/{bankId}/{accountDate}")
	public Object statement(@PathVariable String bookId, @PathVariable String bankId, @PathVariable String accountDate) {
		if (StringUtils.isBlank(bookId) 
				|| StringUtils.isBlank(bankId)
				|| StringUtils.isBlank(accountDate)) {
			throw new LogicException("参数异常!");
		}
		Date date = DateUtils.parseDate(accountDate);
		return bankStatementService.statement(bookId,bankId, date);
	}
	
	/**
	 * 忽略对账
	 * @param id
	 */
	@GetMapping("/bank/statement/ingre/{id}")
	public Object ignore(@PathVariable String id) {
		if (StringUtils.isBlank(id)) {
			throw new LogicException("参数异常!");
		}
		bankStatementService.ignoreStatement(id);
		return null;
	}
	
}
