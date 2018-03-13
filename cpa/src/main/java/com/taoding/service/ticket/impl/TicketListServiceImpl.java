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
import com.taoding.domain.ticket.Ticket;
import com.taoding.domain.ticket.TicketList;
import com.taoding.domain.ticket.TicketListExample;
import com.taoding.domain.ticket.TicketListExample.Criteria;
import com.taoding.domain.ticket.TicketListTemplate;
import com.taoding.domain.ticket.vo.Condition;
import com.taoding.domain.ticket.vo.Template;
import com.taoding.domain.ticket.vo.TicketListInit;
import com.taoding.domain.ticket.vo.TicketListVo;
import com.taoding.mapper.ticket.TicketListDao;
import com.taoding.service.ticket.ListTemplateService;
import com.taoding.service.ticket.TicketListService;
import com.taoding.service.ticket.TicketListTemplateService;
import com.taoding.service.ticket.TicketService;

/**
 * 票据业务接口
 * 
 * @author 刘鑫
 * @category 票据目录业务接口
 *
 */
@Service
@Transactional
public class TicketListServiceImpl implements TicketListService {

	private final static String TICKET_LIST = "cpa_ticket_list";

	@Autowired
	private TicketListDao ticketListDao;

	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketListTemplateService ticketListTemplateService;
	@Autowired
	private ListTemplateService listTemplateService;

	@Override
	public void init(String bookId) throws LogicException {
		if (StringUtils.isBlank(bookId)) {
			throw new LogicException("账簿ID为空!");
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("bookId", bookId);
		condition.put("deleted", FALSE);
		List<TicketList> ticketLists = getTicketLists(condition);
		if (ticketLists != null && ticketLists.size() != 0) {// 如果已经存在目录，则不需要初始化，退出此方法
			throw new LogicException("此账簿已存在目录，不能初始化!");
		}

		ListTemplate listTemplate = listTemplateService.geListTemplate(TICKET_LIST);
		List<TicketListInit> templates = JsonUtil.jsonToList(listTemplate.getTemplateList(), TicketListInit.class);
		for (TicketListInit template : templates) {

			Date date = new Date();
			TicketList ticketList = new TicketList();

			ticketList.setId(UUIDUtils.getUUid());
			// ticketList.setAccountingId(UserUtils.getCurrentUserId());
			ticketList.setAccountingId("");
			ticketList.setBookId(bookId);
			ticketList.setParentId("0");
			List<String> ids = new ArrayList<>();
			ids.add("0");
			ids.add(ticketList.getId());
			ticketList.setParentIds(JsonUtil.objectToJson(ids));
			ticketList.setSubjectContent(JsonUtil.objectToJson(template.getSubjectContent()));
			ticketList.setName(template.getName());
			ticketList.setIsPreset(Byte.valueOf("1"));
			ticketList.setIsDefault(template.getIsDefault() ? TRUE : FALSE);
			ticketList.setCreated(date);
			ticketList.setUpdated(date);

			List<TicketListInit> children = template.getChildren();
			if (children != null && children.size() != 0) {
				init(ticketList, children);
			}
			ticketListDao.insertSelective(ticketList);
		}
	}

	@Override
	public TicketList getTicketListById(String id) {
		return ticketListDao.selectByPrimaryKey(id);
	}
	
	@Override
	public TicketList getTicketListByName(String bookId, String name) {
		TicketList ticketList = null;
		
		TicketListExample example = new TicketListExample();
		Criteria criteria = example.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		criteria.andNameEqualTo(name);
		criteria.andDeletedEqualTo(FALSE);
		List<TicketList> ticketLists = ticketListDao.selectByExample(example);
		if(ticketLists != null && ticketLists.size() != 0)
			ticketList = ticketLists.get(0);
		
		return ticketList;
	}

	@Override
	public List<TicketList> getTicketListsByParentId(String parentId, String bookId) {
		if (StringUtils.isBlank(parentId)) {
			return null;
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("parentId", parentId);
		condition.put("bookId", bookId);
		condition.put("deleted", 0);

		return getTicketLists(condition);
	}

	@Override
	public List<TicketListVo> getTicketListVosByParentId(String parentId, String bookId, Date accountDate) {
		if (StringUtils.isBlank(parentId)) {
			return null;
		}

		Map<String, Object> condition = new HashMap<>();
		condition.put("parentId", parentId);
		condition.put("accountDate", accountDate);
		condition.put("bookId", bookId);
		condition.put("deleted", 0);

		return getTicketListVos(condition);
	}

	@Override
	public List<TicketList> getTicketLists(Map<String, Object> condition) {
		if (condition == null) {
			return null;
		}
		return ticketListDao.selectList(condition);
	}

	@Override
	public List<TicketListVo> getTicketListVos(Map<String, Object> condition) {
		if (condition == null) {
			return null;
		}
		return ticketListDao.selectListVo(condition);
	}

	@Override
	public void insertTicketList(TicketList ticketList) {
		if (StringUtils.isBlank(ticketList.getName())) {
			throw new LogicException("名称不能为空!");
		}
		String parentId = ticketList.getParentId();
		if (StringUtils.isBlank(parentId) || parentId.equals("0")) {
			throw new LogicException("不能创建一级目录!");
		}

		Date date = new Date();
		TicketList parent = getTicketListById(parentId);

		String id = UUIDUtils.getUUid();
		ticketList.setId(id);
//		ticketList.setAccountingId(UserUtils.getCurrentUserId());
		ticketList.setAccountingId("");
		List<String> parentIds = JsonUtil.jsonToList(parent.getParentIds(), String.class);
		parentIds.add(id);
		ticketList.setParentIds(JsonUtil.objectToJson(parentIds));
		ticketList.setIsPreset(FALSE);

		// 设置预设的科目
		int level = getTicketListLevel(ticketList);
		if (level <= 2) {
			TicketList defaultTicketList = defaultTicketList(ticketList.getParentId());
			ticketList.setSubjectContent(defaultTicketList.getSubjectContent());
		} else {
			ticketList.setSubjectContent(parent.getSubjectContent());
		}

		ticketList.setCreated(date);
		ticketList.setUpdated(date);

		ticketListDao.insertSelective(ticketList);
	}

	@Override
	public void updateTicketList(TicketList ticketList) {
		String name = ticketList.getName();
		if (StringUtils.isNotBlank(name)) {
			if (ticketList.getIsPreset().byteValue() == 1) {
				throw new LogicException("预设目录不能修改名称!");
			}
		}
		ticketList.setUpdated(new Date());
		ticketListDao.updateByPrimaryKeySelective(ticketList);
	}

	@Override
	public void deleteTicketList(String id, Date date) {
		if (StringUtils.isBlank(id)) {
			throw new LogicException("参数非法!");
		}

		TicketList ticketList = ticketListDao.selectByPrimaryKey(id);
		if (ticketList.getIsPreset().byteValue() == 1) {
			throw new LogicException("不能删除预设的目录!");
		}

		// 判断是否需要修改模板
		TicketListTemplate ticketListTemplate = ticketListTemplateService.getTicketListTemplate(ticketList.getBookId());
		updateTemplate(ticketListTemplate, id);

		// 删除子节点
		List<TicketList> children = getTicketListsByParentId(id, null);
		if (children != null && children.size() != 0) {
			deleteTicketList(children, ticketListTemplate, date);
		}

		// 将此目录下所有的票据更新为作废状态
		Condition condition = new Condition(false, null, null, null);
		List<Ticket> tickets = ticketService.getTicketsByListId(ticketList.getBookId(), id, date, condition);
		for (Ticket ticket : tickets) {
			ticket.setIsVoid(TRUE);
			ticket.setUpdated(new Date());
			ticketService.updateTicket(ticket);
		}

		// 修改模板
		ticketListTemplateService.updateTemplate(ticketListTemplate);
		// 删除目录
		ticketListDao.deleteByPrimaryKey(id);

	}
	
	@Override
	public void deleteAll(String bookId) {
		TicketList ticketList = new TicketList();
		ticketList.setDeleted(TRUE);
		ticketList.setUpdated(new Date());
		TicketListExample example = new TicketListExample();
		Criteria criteria = example.createCriteria();
		criteria.andBookIdEqualTo(bookId);
		ticketListDao.updateByExampleSelective(ticketList, example);
	}

	private void deleteTicketList(List<TicketList> ticketLists, TicketListTemplate ticketListTemplate, Date date) {
		for (TicketList ticketList : ticketLists) {
			// 判断是否需要修改模板
			updateTemplate(ticketListTemplate, ticketList.getId());

			// 将此目录下所有的票据更新为作废状态
			Condition condition = new Condition(false, false, null, null);
			List<Ticket> tickets = ticketService.getTicketsByListId(ticketList.getBookId(), ticketList.getId(), date,
					condition);
			Date now = new Date();
			for (Ticket ticket : tickets) {
				ticket.setIsVoid(TRUE);
				ticket.setUpdated(now);
				ticketService.updateTicket(ticket);
			}

			// 如果存在子目录集合 ，重载方法
			List<TicketList> children = getTicketListsByParentId(ticketList.getId(), null);
			if (children != null && children.size() != 0) {
				deleteTicketList(children, ticketListTemplate, date);
			}

			// 删除目录
			ticketListDao.deleteByPrimaryKey(ticketList.getId());
		}
	}

	/**
	 * 修改目标目录 的模板规则
	 * 
	 * @param templates
	 * @param listId
	 * @return
	 */
	private void updateTemplate(TicketListTemplate ticketListTemplate, String listId) {

		String template = ticketListTemplate.getTemplate();
		String templating = ticketListTemplate.getTemplating();

		List<Template> templates = JsonUtil.jsonToList(template, Template.class);
		List<Template> list = ticketListTemplateService.getTemplateByListId(templates, listId);
		for (Template t : list) {
			templates.remove(t);
		}
		ticketListTemplate.setTemplate(JsonUtil.objectToJson(templates));

		templates = JsonUtil.jsonToList(templating, Template.class);
		list = ticketListTemplateService.getTemplateByListId(templates, listId);
		for (Template t : list) {
			templates.remove(t);
		}
		ticketListTemplate.setTemplating(JsonUtil.objectToJson(templates));

	}

	/**
	 * 初始化一个模板列表
	 * 
	 * @param templates
	 */
	private void init(TicketList parent, List<TicketListInit> templates) {

		for (TicketListInit template : templates) {
			TicketList ticketList = new TicketList();

			ticketList.setId(UUIDUtils.getUUid());
			ticketList.setAccountingId(parent.getAccountingId());
			ticketList.setBookId(parent.getBookId());
			ticketList.setParentId(parent.getId());
			List<String> ids = JsonUtil.jsonToList(parent.getParentIds(), String.class);
			ids.add(ticketList.getId());
			ticketList.setParentIds(JsonUtil.objectToJson(ids));
			ticketList.setSubjectContent(JsonUtil.objectToJson(template.getSubjectContent()));
			ticketList.setName(template.getName());
			ticketList.setIsPreset(parent.getIsPreset());
			ticketList.setIsDefault(template.getIsDefault() ? TRUE : FALSE);
			ticketList.setCreated(parent.getCreated());
			ticketList.setUpdated(parent.getUpdated());

			List<TicketListInit> children = template.getChildren();
			if (children != null && children.size() != 0) {
				init(ticketList, children);
			}

			ticketListDao.insertSelective(ticketList);
		}

	}

	private TicketList defaultTicketList(String parentId) {
		TicketList ticketList = null;
		List<TicketList> ticketLists = getTicketListsByParentId(parentId, null);
		for (TicketList t : ticketLists) {
			if (t.getIsDefault() == TRUE) {
				ticketList = t;
				break;
			}
		}
		return ticketList;
	}

	@Override
	public int getTicketListLevel(TicketList ticketList) {
		int level = 0;
		List<String> parentIds = JsonUtil.jsonToList(ticketList.getParentIds(), String.class);
		for (int i = 0; i < parentIds.size(); i++) {
			if (ticketList.getId().equals(parentIds.get(i))) {
				level = ++ i;
				break;
			}
		}
		return level;
	}
	
	public static void main(String[] args) {
		String parentIds = "[\"0\",\"f0b9ba4dbdfd4493a003c8b6b0c4f348\",\"0034c223cbdc422796679952358e7a89\"]";
		List<String> jsonToList = JsonUtil.jsonToList(parentIds, String.class);	
		for (String str : jsonToList) {
			System.out.println(str);
		}
	}
	
}
