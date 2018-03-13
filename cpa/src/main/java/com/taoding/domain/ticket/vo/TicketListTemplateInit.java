package com.taoding.domain.ticket.vo;

import lombok.Data;

/**
 * 初始化票据科目-摘要模板的中间数据
 * @author 刘鑫
 *
 */
@Data
public class TicketListTemplateInit {
	
	private String parentName;	// 一级目录名称
	
	private String name;	//目录名称
	
	private String[] summary;	// 所属的摘要集合

}
