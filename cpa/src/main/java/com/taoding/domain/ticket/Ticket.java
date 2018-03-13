package com.taoding.domain.ticket;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;

import lombok.Data;

@Data
@ValidationBean
public class Ticket {
	// 主键
	private String id;
	// 票据编号
	private String ticketNo;
	// 凭证ID
	private String voucherId;
	// 客户ID
	@NotEmpty(message="客户ID不能为空")
	private String customerId;
	// 账簿ID
	@NotEmpty(message="账簿ID不能为空")
	private String bookId;
	// 会计ID
	private String accountingId;
	// 所属目录
	private String listId;
	// 所属目录组
	private String listIds;
	// 科目内容
	private String subjectContent;
	// 票据类型 0.银行票据 1.增值税普通发票 2.增值税专用发票 3.银行对账单 4.普通发票 5.证明
//	@NotEmpty(message="发票类型不能为空")
	private Byte type;
	// 票据号码
	private String ticketKey;
	// 票据代码
	private String ticketCode;
	// 票据检验码
	private String ticketCheckCode;
	// 票据密码
	private String password;
	/**付款方名称*/
	private String payerName;
	// 付款方纳税识别号
	private String payerCode;
	// 付款方地址及电话
	private String payerAddress;
	// 付款付开户行地址及账号
	private String payerAccount;
	// 收款方名称
	private String payeeName;
	// 收款方纳税识别号
	private String payeeCode;
	// 收款方地址及电话
	private String payeeAddress;
	// 收款方开户行地址及账号
	private String payeeAccount;
	// 所属省份
	private String province;
	// 所属城市
	private String city;
	// 摘要关键字
	private String summaryWord;
	// 金额
	private BigDecimal amount;
	// 备注
	private String remarks;
	// 摘要明细
	private String summary;
	// 收款人
	private String payee;
	// 复核
	private String recheck;
	// 开票人
	private String drawer;
	// 销售人
	private String seller;
	// 税额
	private BigDecimal tax;
	// 税率
	private BigDecimal taxRate;
	// 账期
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date accountDate;
	// 开票日期
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date ticketDate;
	// 差额征税
	private BigDecimal diffTax;
	// 总金额
	private BigDecimal totalAmount;
	// 是否是问题票据
	private Byte isLllegal;
	// 问题票据说明
	private String lllegalExplain;
	// 票据图片URL
	private String ticketUrl;
	// 是否真伪
	private Byte isReal;
	// 代开发票
	private Byte isBehalfOf;
	// 是否已识别(0. 否 1.是)
	private Byte isIdentify;
	// 是否作废(0. 否 1.是)
	private Byte isVoid;
	// 是否已做账
	private Byte completed;
	// 是否删除
	private Byte deleted;
	// 创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date created;
	// 更新时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date updated;
}