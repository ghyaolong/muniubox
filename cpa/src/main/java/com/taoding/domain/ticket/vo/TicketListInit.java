package com.taoding.domain.ticket.vo;

import java.util.List;

import lombok.Data;

/**
 * 初始化票据目录的中间数据
 * @author 刘鑫
 *
 */
@Data
public class TicketListInit {
	
	 // 科目内容
    private List<Subject> subjectContent;
    // 名称
    private String name;
    // 是否为默认的票据目录
    private Boolean isDefault;
    
    private List<TicketListInit> children;

}
