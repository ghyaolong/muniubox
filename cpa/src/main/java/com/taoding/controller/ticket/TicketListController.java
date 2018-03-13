package com.taoding.controller.ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.accountingbook.AccountingBook;
import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.vo.TicketListVo;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.ticket.TicketListService;

/**
 * 票据目录 API
 * 
 * @author 刘鑫
 *
 */
@RestController
@RequestMapping("/ticketList")
public class TicketListController extends BaseController {

	@Autowired
	private TicketListService ticketListService;
	@Autowired
	private AccountingBookService accountingBookService;

	/**
	 * 按照目录ID更新目录的信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/get")
	public Object getTicketList(String id) {
		Map<String, Object> condition = new HashMap<>();
		condition.put("id", id);
		return ticketListService.getTicketListVos(condition).get(0);
	}

	/**
	 * 获得一个目标账簿的目录的列表
	 * 
	 * @param bookId
	 *            账簿ID 可选参数
	 * @param parentId
	 *            目录父ID
	 * @return
	 */
	@GetMapping("/list")
	public Object getTicketLists(String bookId, String parentId) {
		if (StringUtils.isBlank(bookId)) {
			throw new LogicException("账簿ID为空或者目录父ID为空!");
		}
		
		List<TicketList> ticketLists = null;
		if (StringUtils.isNotBlank(parentId)) {
			ticketLists = ticketListService.getTicketListsByParentId(parentId, bookId);
		} else {
			Map<String, Object> condition = new HashMap<>();
			condition.put("bookId", bookId);
			condition.put("deleted", 0);
			ticketLists = ticketListService.getTicketLists(condition);
		}

		return ticketLists;
	}

	/**
	 * 新建自定义目录
	 * 
	 * @param name
	 * @return
	 */
	@GetMapping("/new")
	public void insertTicketList(String bookId, String name, String parentId) {
		if (StringUtils.isBlank(bookId) || StringUtils.isBlank(name) || StringUtils.isBlank(parentId)) {
			throw new LogicException("参数为空!");
		}
		AccountingBook book = accountingBookService.get(bookId);
		if (book == null) {
			throw new LogicException("无此账簿!");
		}
		TicketList ticketList = new TicketList();
		ticketList.setBookId(bookId);
		ticketList.setAccountingId("");
		ticketList.setName(name);
		ticketList.setParentId(parentId);
		ticketListService.insertTicketList(ticketList);
	}

	/**
	 * 编辑一个目录
	 * 
	 * @param ticketList
	 * @return
	 */
	@PostMapping("/edit")
	public void updateTicketList(@RequestBody TicketListVo vo) {
		TicketList ticketList = new TicketList();
		BeanUtils.copyProperties(vo, ticketList);
		ticketListService.updateTicketList(ticketList);
	}

	/**
	 * 删除一个自定义目录
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/delete")
	public void deleteTicketList(String id, String date) {
		if (StringUtils.isBlank(id) || StringUtils.isBlank(date)) {
			throw new LogicException("参数异常!");
		}
		ticketListService.deleteTicketList(id, DateUtils.parseDate(date));
	}

}
