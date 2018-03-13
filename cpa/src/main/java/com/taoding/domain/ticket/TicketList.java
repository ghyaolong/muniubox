package com.taoding.domain.ticket;

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;

import lombok.Data;

@Data
@ValidationBean
public class TicketList {
	// 主键
    private String id;
    // 账簿ID
    @NotEmpty(message="账簿ID不能为空")
    private String bookId;
    // 会计ID
    private String accountingId;
    // 父节点ID
    private String parentId;
    // 父节点组
    private String parentIds;
    // 科目内容
    private String subjectContent;
    // 名称
    @NotEmpty(message="名称不能为空")
    private String name;
    // 结算方式(0,往来款结算 1.现金结算 2.第三方支付)
    private Byte clearingType;
    // 凭证生产的策略(0.无策略规则 1.合并分录 2.合并凭证)
    private Byte proofStrategy;
    // 辅助核算项 (0.false 1.true)
    private Byte usinessAccounting;
    // 是否为预设
    private Byte isPreset;
    // 是否为默认票据目录
    private Byte isDefault;
    // 删除
    private Byte deleted;
    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date created;
    // 更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updated;
    
}