package com.taoding.domain.subject;

import java.util.List;

import lombok.Data;
import lombok.ToString;

import com.taoding.common.excel.ExcelField;

@Data
@ToString
public class CertificateVO {
	
	@ExcelField(title = "日期")
	private String date;
	
	@ExcelField(title = "凭证号数")
	private String certificateNum;
	
	@ExcelField(title = "科目编码")
	private String subjectCode;
	
	@ExcelField(title = "摘要")
	private String summary;
	
	@ExcelField(title = "凭科目名称")
	private String subjectNum;
	
	@ExcelField(title = "方向")
	private String direction;
	
	@ExcelField(title = "金额")
	private String amount;

	public CertificateVO() {
		super();
	}

	public CertificateVO(String date, String certificateNum,
			String subjectCode, String summary, String subjectNum,
			String direction, String amount) {
		super();
		this.date = date;
		this.certificateNum = certificateNum;
		this.subjectCode = subjectCode;
		this.summary = summary;
		this.subjectNum = subjectNum;
		this.direction = direction;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "CertificateVO [date=" + date + ", certificateNum="
				+ certificateNum + ", subjectCode=" + subjectCode
				+ ", summary=" + summary + ", subjectNum=" + subjectNum
				+ ", direction=" + direction + ", amount=" + amount + "]";
	}
	
	

}
