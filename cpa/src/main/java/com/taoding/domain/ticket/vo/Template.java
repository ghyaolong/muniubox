package com.taoding.domain.ticket.vo;

import lombok.Data;

/**
 * 模板中间模型-数据库中以json字符串形式存在
 * 
 * @author 刘鑫
 *
 */
@Data
public class Template {

	// 是否属于银行票据
	private boolean isBankTicket;

	// 是否属于银行对账单
	private boolean isAccountStatement;

	// 是否属于证明
	private boolean isCertify;

	// 摘要
	private String summary;

	// 所属目录ID
	private String listId;

	// 所属目录ID组
	private String listIds;

}
