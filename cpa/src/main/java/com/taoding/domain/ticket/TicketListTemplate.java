package com.taoding.domain.ticket;

import java.util.Date;

import lombok.Data;

/**
 * 目录 <--> 摘要之间的关系模板，Datebase 中为对应的 json 字符串
 * 在程序中使用此类做数据的操作，最终转换成json字符串再持久化
 * @author 刘鑫
 *
 */
@Data
public class TicketListTemplate {
	
	// 主键
    private String id;
    // 账簿ID
    private String bookId;
    // 目录:摘要模板,例: [{"abract":"飞机票","listId":123,"listIds":[12,123],"bankTicket":true}]
    private String template;
    // 目录:摘要临时模板
    private String templating;
    // 摘要黑名单
    private String summaryBlacklist;
    // 是否开启按照税率分类整理目录
    private byte isRestByTax;
    // 删除
    private Byte deleted;
    // 创建时间
    private Date created;
    // 更新时间
    private Date updated;
    
}