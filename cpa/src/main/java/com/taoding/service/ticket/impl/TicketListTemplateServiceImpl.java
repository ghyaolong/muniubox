package com.taoding.service.ticket.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.JsonUtil;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UUIDUtils;
import com.taoding.domain.ticket.ListTemplate;
import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.TicketListTemplate;
import com.taoding.domain.ticket.vo.Template;
import com.taoding.domain.ticket.vo.TicketListTemplateInit;
import com.taoding.mapper.ticket.TicketListTemplateDao;
import com.taoding.service.ticket.ListTemplateService;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketListTemplateService;

@Service
@Transactional
public class TicketListTemplateServiceImpl implements TicketListTemplateService {

	private final static String TABLE_NAME = "cpa_ticket_list_template";

	@Autowired
	private TicketListTemplateDao ticketListTemplateDao;

	@Autowired
	private TicketListService ticketListService;
	@Autowired
	private ListTemplateService listTemplateService;

	@Override
	public void init(String bookId) {
		ListTemplate listTemplate = listTemplateService.geListTemplate(TABLE_NAME);
		if (listTemplate == null) {
			throw new LogicException("请先配置预设的模板!");
		}

		TicketListTemplate ticketListTemplate = getTicketListTemplate(bookId);
		if (ticketListTemplate != null) {
			throw new LogicException("已存在模板不需要初始化!");
		}

		List<Template> templates = new ArrayList<>();

		List<TicketListTemplateInit> inits = JsonUtil.jsonToList(listTemplate.getTemplateList(),
				TicketListTemplateInit.class);
		for (TicketListTemplateInit init : inits) {
			Map<String, Object> condition = new HashMap<>();
			condition.put("bookId", bookId);
			condition.put("name", init.getParentName());
			condition.put("deleted", 0);
			TicketList parent = ticketListService.getTicketLists(condition).get(0);	//一级目录
			List<TicketList> ticketLists = ticketListService.getTicketListsByParentId(parent.getId(), null);
			TicketList ticketList = null;
			for (TicketList t : ticketLists) {
				if (init.getName().equals(t.getName())) {
					ticketList = t;
					break;
				}
			}

			for (String summary : init.getSummary()) {
				Template template = new Template();
				template.setBankTicket(parent.getName().equals("银行票据") ? true : false);
				template.setAccountStatement(parent.getName().equals("银行对账单") ? true : false);
				template.setCertify(parent.getName().equals("证明") ? true : false);
				template.setListId(ticketList.getId());
				template.setListIds(ticketList.getParentIds());
				template.setSummary(summary);
				templates.add(template);
			}
		}

		ticketListTemplate = new TicketListTemplate();
		ticketListTemplate.setId(UUIDUtils.getUUid());
		ticketListTemplate.setBookId(bookId);
		ticketListTemplate.setTemplate(JsonUtil.objectToJson(templates));
		ticketListTemplate.setTemplating(JsonUtil.objectToJson(templates));
		ticketListTemplate.setIsRestByTax(TRUE);
		Date date = new Date();
		ticketListTemplate.setCreated(date);
		ticketListTemplate.setUpdated(date);
		ticketListTemplateDao.insertSelective(ticketListTemplate);
	}

	@Override
	public TicketListTemplate getTicketListTemplate(String bookId) {
		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", bookId);
		condition.put("deleted", 0);

		List<TicketListTemplate> list = ticketListTemplateDao.selectList(condition);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<Template> getTemplate(String bookId) {
		TicketListTemplate template = getTicketListTemplate(bookId);
		if (template == null) {
			return null;
		}
		return JsonUtil.jsonToList(template.getTemplate(), Template.class);
	}

	@Override
	public List<Template> getTemplating(String bookId) {
		TicketListTemplate template = getTicketListTemplate(bookId);
		if (template == null) {
			return null;
		}
		return JsonUtil.jsonToList(template.getTemplating(), Template.class);
	}

	@Override
	public List<Template> getTemplateByListId(List<Template> templates, String listId) {
		List<Template> result = new ArrayList<>();
		for (Template template : templates) {
			if (template.getListId().equals(listId)) {
				result.add(template);
			}
		}
		return result;
	}

	@Override
	public void updateTemplate(TicketListTemplate template) {
		template.setUpdated(new Date());
		ticketListTemplateDao.updateByPrimaryKeySelective(template);
	}

	@Override
	public void abstractBlacklist(String bookId, String[] blacklist) {
		if (StringUtils.isBlank(bookId)) {
			throw new LogicException("参数异常!");
		}
		TicketListTemplate template = getTicketListTemplate(bookId);
		if (template == null) {
			throw new LogicException("数据异常!");
		}
		
		String summaryBlacklist = template.getSummaryBlacklist();
		List<String> list = StringUtils.isBlank(summaryBlacklist) ? new ArrayList<>()
				: JsonUtil.jsonToList(summaryBlacklist, String.class);

		for (String blackName : blacklist) {
			list.add(blackName);
		}
		
		template.setSummaryBlacklist(JsonUtil.objectToJson(list));
		template.setUpdated(new Date());
		ticketListTemplateDao.updateByPrimaryKeySelective(template);
	}
	
	@Override
	public void deleteAll(String bookId) {
		TicketListTemplate ticketListTemplate = getTicketListTemplate(bookId);
		if (ticketListTemplate != null) {
			ticketListTemplate.setDeleted(TRUE);
			ticketListTemplate.setUpdated(new Date());
			ticketListTemplateDao.updateByPrimaryKeySelective(ticketListTemplate);
		}
	}

}
