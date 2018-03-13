package com.taoding.domain.settleaccount;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 企业期末结账 基础配置
 * @author admin
 *
 */
@Data
@ToString
public class CpaSettleAccountSubjectBasic {
	
	// id
	private String id;
	
	// 企业类型
	private String enterpriseType;
	
	// sub_key 固定
	private String subKey;
	
	private String subName;
	
	// 科目 id
	private String subjectId;
	
	// 科目编号
	private String subjectNo;
	
	// 科目名称
	private String subjectName;
	
	// 操作人
	private String createBy;
	
	// 创建时间
	private Date createDate;
}
