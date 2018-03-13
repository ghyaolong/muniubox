package com.taoding.service.ticket;

import com.taoding.domain.ticket.ListTemplate;

/**
 * 目录模板业务接口
 * @author LX-PC
 * @category 目录模板业务接口
 */
public interface ListTemplateService {
	
	/**
	 * 插入一个新的模板
	 * @param tableName 表名，记录词表的模板
	 * @param template 模板，此为json字符串
	 */
	public void insertTemplate(String tableName, String template);
	
	/**
	 * 按照表名获取一条模板
	 * @param tableName
	 * @return
	 */
	public ListTemplate geListTemplate(String tableName);

}
