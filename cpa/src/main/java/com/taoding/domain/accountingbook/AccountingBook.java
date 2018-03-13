package com.taoding.domain.accountingbook;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.utils.DateJsonDeserializer;
import com.taoding.common.utils.DateUtils;

@ValidationBean
@Data
public class AccountingBook extends DataEntity<AccountingBook> {

	public static final int ACCOUNT_STATUS_NO = 0;// "未建账";
	public static final int ACCOUNT_STATUS_ALREADY = 1;// "已建账";
	public static final int ACCOUNT_STATUS_NO_START = 2;// "未开始";
	public static final int ACCOUNT_STATUS_WORKING = 3;// "进行中";
	public static final int ACCOUNT_STATUS_WARNING = 4;// "预警器";
	public static final int ACCOUNT_STATUS_COMPLETED = 5;// "已完成";

	@NotEmpty(message = "客户不能为空！")
	private String customerInfoId;

	@JsonDeserialize(using = DateJsonDeserializer.class)
	@NotNull(message = "账套初始日期不能为空！")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date accountingStartDate;

	//1 小规模纳税人 2一般纳税人
	@NotEmpty(message = "纳税人性质不能为空！")
	private String taxpayerPropertyId;

	//1小企业会计准则  2企业会计准则
	@NotEmpty(message = "会计制度不能为空！")
	private String accountingSystemId;// 小企业会计准则 企业会计准则

	// @NotEmpty(message="授权状态  不能为空！")
	private Integer authoriseStatus;// 授权状态 0 未授权 1 已授权

	// @NotEmpty(message="指派状态不能为空！")
	private Integer assignedStatus;// 指派状态 0 已指派 1 未指派

	@NotEmpty(message = "助记码不能为空！")
	private String code;// 助记码

	private Date currentPeriod;// 当前账期 默认为 每月1号

	private Integer accountStatus; // 记账状况 可能的状态：0 未建账，1 已建账 2 未开始，
									// 3进行中，4预警期，5已完成

	/*
	 * 扩充字段
	 */
	private String accountingSystemName; // 小企业会计准则 企业会计准则
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date accountsDate; // 建账时间

	public static String findAccountStatusName(Integer accountStatus ,Date currentPeriod) {
		

		if (null == accountStatus) {
			return "未建账";
		}
	     if(null == currentPeriod){
	    	 return "";
	     }
			
	     String status = "";
	     
		switch (accountStatus) {
		case AccountingBook.ACCOUNT_STATUS_NO:
			status = "未建账";
			break;
		case AccountingBook.ACCOUNT_STATUS_ALREADY:
			status = "已建账";
			break;
		case AccountingBook.ACCOUNT_STATUS_NO_START:
			if (null != currentPeriod) {
				status = DateUtils.formatDate(currentPeriod, "MM") + "月未开始";
			}
			status = "未开始";
			break;
		case AccountingBook.ACCOUNT_STATUS_WORKING:
			if (null !=currentPeriod) {
				status = DateUtils.formatDate(currentPeriod, "MM") + "月进行中";
			}
			break;
		case AccountingBook.ACCOUNT_STATUS_WARNING:
			if (null != currentPeriod) {
				status = DateUtils.formatDate(currentPeriod, "MM") + "月预警器";
			}
			break;
		case AccountingBook.ACCOUNT_STATUS_COMPLETED:
			if (null != currentPeriod) {
				status = DateUtils.formatDate(currentPeriod, "MM") + "月已完成";
			}
			status = "已完成";
			break;
		default:
			status = "";
		}
		
		return status;
	}
}
