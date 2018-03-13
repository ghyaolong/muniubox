package com.taoding.controller.ticket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.response.Response;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.vo.TicketListVo;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketService;

/**
 * 票据API
 * 
 * @author 刘鑫
 *
 */
@RestController
@RequestMapping("/ticket")
public class TicketController extends BaseController {
	
	private enum EditType {
		BASICS, CROSS_PERIOD, TO_VOID
	}

	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketListService ticketListService;

	/**
	 * 添加一张票据
	 * 
	 * @param ticket
	 * @return
	 */
//	@PostMapping("/new")
	@Deprecated
	public void insertTicket(@RequestBody Ticket ticket) {
		ticketService.insertTicket(ticket);
	}

	/**
	 * 修改票据属性
	 * 
	 * @param ticket
	 * @return
	 */
	@PostMapping("/edit")
	public Object updateTicket(@RequestBody Ticket ticket) {
		ticketService.updateTicket(ticket);
		return null;
	}

	/**
	 * 根据父节点获取票据页面的目录及目录的下属票据
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public Object getListsAndTickets(HttpServletRequest request) {
		Map<String, Object> condition = getRequestParams(request);
		String listId = (String) condition.get("listId");
		String completed = (String) condition.get("completed");
		if (StringUtils.isBlank(listId) || StringUtils.isBlank(completed)) {
			throw new LogicException("目录ID为空,或请求类型错误!");
		}
		condition.put("isVoid", 0);
		condition.put("deleted", 0);

		Map<String, Object> result = new HashMap<>();
		List<Ticket> tickets = ticketService.getTickets(condition);
		List<TicketListVo> ticketListVos = ticketListService.getTicketListVosByParentId(listId,
				(String) condition.get("bookId"), DateUtils.parseDate(condition.get("accountDate")));

		result.put("tickets", tickets);
		result.put("ticketLists", ticketListVos);

		if (listId.equals("0")) {
			long totalCount = ticketService.countByAccountDate((String) condition.get("bookId"),
					DateUtils.parseDate(condition.get("accountDate")));
			result.put("totalCount", totalCount);
		}

		return result;
	}

	/**
	 * 获得一张或多张票据的信息，支持条件检索功能
	 * 
	 * @return
	 */
	@GetMapping("/get")
	public Object getTickets(HttpServletRequest request) {
		Map<String, Object> condition = getRequestParams(request);
		condition.put("deleted", 0);
		return ticketService.getTickets(condition);
	}

	/**
	 * 获得账期内所有票据的图片
	 * 
	 * @return
	 */
	@GetMapping("/images")
	public Object getImagesOfTicket(String bookId, String date) {
		if (StringUtils.isBlank(bookId) || StringUtils.isBlank(date)) {
			throw new LogicException("参数错误!");
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", bookId);
		condition.put("date", DateUtils.parseDate(date));
		condition.put("deleted", 0);
		List<Ticket> tickets = ticketService.getTickets(condition);

		List<Map<String, Object>> result = new ArrayList<>();
		for (Ticket ticket : tickets) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", ticket.getId());
			map.put("ticketNo", ticket.getTicketNo());
			map.put("ticketUrl", ticket.getTicketUrl());
			result.add(map);
		}
		return result;
	}

	/**
	 * 票据移动至目标目录下
	 * 
	 * @param bookId
	 * @param id
	 * @param listId
	 */
	@GetMapping("/move")
	public Object moveTicketToList(String bookId, String id, String listId) {
		ticketService.moveTicketToList(bookId, id, listId);
		return null;
	}
	
	/**
	 * 一组票据移动至目标目录下
	 * 
	 * @param bookId	账簿ID
	 * @param ids		需要移动的票据ID集合
	 * @param listId	目标目录ID
	 */
	@PostMapping("/moves")
	public Object moveTicketsToList(String bookId, @RequestBody List<String> ids, String listId) {
		ticketService.moveTicketsToList(bookId, ids.toArray(new String[ids.size()]), listId);
		return null;
	}
	
	/**
	 * 将指定目录下的所有票据移动至目标目录下
	 * @param bookId	账簿ID
	 * @param resource	制定的目录ID	
	 * @param listId	目标目录ID
	 * @param accountDate	账期
	 */
	@GetMapping("/moves")
	public Object moveTicketsToList(String bookId, String resource, String listId, String accountDate) {
		ticketService.moveTicketsToList(bookId, resource, listId, DateUtils.parseDate(accountDate));
		return null;
	}
	
	/**
	 * 按照模板重新整理当前账期内所有票据的所属目录
	 * 
	 * @param bookId
	 * @param accountDate
	 *            账期
	 */
	@GetMapping("/reset")
	public Object resetByTemplate(String bookId, String listId, String accountDate) {
		if (StringUtils.isBlank(bookId) || accountDate == null) {
			throw new LogicException("参数异常!");
		}

		if (StringUtils.isBlank(listId)) {
			ticketService.reset(bookId, DateUtils.parseDate(accountDate));
		} else {
			ticketService.resetByList(bookId, listId, DateUtils.parseDate(accountDate));
		}
		
		return null;
	}

	@Override
	protected Map<String, Object> getRequestParams(HttpServletRequest request) {
		Map<String, Object> condition = super.getRequestParams(request);
		String bookId = (String) condition.get("bookId");
		Date date = DateUtils.parseDate(condition.get("accountDate"));
		if (StringUtils.isBlank(bookId) || date == null) {
			throw new LogicException("账簿ID为空，或账期输入不正确!");
		}
		return condition;
	}
	
}
