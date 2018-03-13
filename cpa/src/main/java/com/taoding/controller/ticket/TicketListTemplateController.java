package com.taoding.controller.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.ticket.TicketListTemplate;
import com.taoding.service.ticket.TicketListTemplateService;

/**
 * 票据目录模板API
 * @author 刘鑫
 *
 */
@RestController
@RequestMapping("/ticket/template")
public class TicketListTemplateController {
	
	@Autowired
	private TicketListTemplateService ticketListTemplateService;
	
	/**
	 * 添加摘要黑名单
	 * @param blacklist
	 * @return
	 */
	@GetMapping("/blacklist/new")
	public void abstractBlacklist(String bookId, String[] blacklist) {
		ticketListTemplateService.abstractBlacklist(bookId, blacklist);
	}
	
	/**
	 * 黑名单列表
	 * @param bookId
	 * @return
	 */
	@GetMapping("/blacklist/list")
	public Object getAbstractBlackList(String bookId) {
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		return JsonUtil.jsonToList(ticketListTemplate.getSummaryBlacklist(), String.class);
	}
	
	/**
	 * 设置	是否按照税率整理票据规则	开关
	 * @param bookId	账簿ID
	 * @param value		value = 0 --> off value = 1 --> on
	 */
	@GetMapping("/rest/switch")
	public void updateIsRestByTax(String bookId, Integer value) {
		if (StringUtils.isBlank(bookId)
				|| value == null
				|| value < 0
				|| value > 1) {
			throw new LogicException("参数异常");
		}
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(bookId);
		ticketListTemplate.setIsRestByTax(value.byteValue());
		ticketListTemplateService.updateTemplate(ticketListTemplate);
	}

}
