package com.taoding.service.ticket;

import java.util.List;

import com.taoding.domain.ticket.TicketListTemplate;
import com.taoding.domain.ticket.vo.Template;

/**
 * 票据模板业务接口
 * @author 刘鑫
 * @category 票据模板业务接口
 */
public interface TicketListTemplateService extends BaseService {
	
	/**
	 * 获得一条模板记录
	 * @param bookId 账簿ID
	 * @return {@link TicketListTemplate}
	 */
	public TicketListTemplate getTicketListTemplate(String bookId);
	
	/**
	 * 获得一个模板(与获得一个临时模板不能重复使用，原因:重复对数据库查询业务造成查冗余)
	 * 如果要同时获得临时模板请使用对应的API getTicketListTemplate
	 * @param bookId 账簿ID
	 * @return 返回一个模板集合{@link List}
	 */
	public List<Template> getTemplate(String bookId);
	
	/**
	 * 获得一个临时模板(与获得一个模板不能重复使用，原因:重复对数据库查询业务造成查询冗余)
	 * 如果要同时获得模板请使用对应的API getTicketListTemplate
	 * @param bookId 账簿ID
	 * @return 返回一个临时模板集合{@link List}
	 */
	public List<Template> getTemplating(String bookId);
	
	/**
	 * 根据目录ID 获取此目录对应的 摘要 模板信息
	 * @param templates 所有模板列表
	 * @param listId 目录ID
	 * @return
	 */
	public List<Template> getTemplateByListId(List<Template> templates, String listId);
	
	/**
	 * 修改模板记录
	 * @param template
	 * @return
	 */
	public void updateTemplate(TicketListTemplate template);
	
	/**
	 * 向模板中添加摘要黑名单
	 * @param bookId 账簿ID
	 * @param blacklist 黑名单
	 * @return
	 */
	public void abstractBlacklist(String bookId, String[] blacklist);
	
}
