package com.taoding.service.ticket;

import java.util.Date;
import java.util.List;

import com.taoding.common.exception.LogicException;
import com.taoding.domain.ticket.BankStatement;
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.vo.BankStatementVo;

/**
 * 银行对账相关业务
 * @author LX-PC
 *
 */
public interface BankStatementService extends BaseService {
	
	/**
	 * 获取目标账簿和目标账期内的对账单列表
	 * @param bookId	账簿ID
	 * @param accountDate 	账期
	 * @return
	 */
	public List<BankStatement> getBankStatements(String bookId, Date accountDate);
	
	/**
	 * 获取目标账簿和目标账期及目标银行内的对账单列表
	 * @param bookId	账簿ID
	 * @param bankId	银行ID
	 * @param accountDate 	账期
	 * @return
	 */
	public List<BankStatement> getBankStatements(String bookId, String bankId, Date accountDate);
	
	/**
	 * 根据对账单票据对象批量创建对账信息
	 * @param ticket
	 * @throws LogicException 当票据的对账信息解析失败时抛出此异常
	 */
	public void insertBankStatement(Ticket ticket) throws LogicException;
	
	/**
	 * 忽略此条记录参与对账，将此条记录账期修改至下一个账期参与对账
	 * @param id 目标ID
	 */
	public void ignoreStatement(String id);
	
	/**
	 * 对目标账期内的账单进行对账
	 * @param bookId	账簿ID
	 * @param bankId 	银行ID
	 * @param accountDate	账期
	 * @return
	 */
	public List<BankStatementVo> statement(String bookId, String bankId, Date accountDate);

}
