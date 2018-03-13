package com.taoding.domain.voucher;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

/**
 * 凭证批注
 * 
 * @author czy 2017年11月28日 上午11:25:04
 */
@Data
@ToString
@ValidationBean
public class CpaVoucherPostil extends DataEntity<CpaVoucherPostil> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3718130058438571974L;

	// 凭证ID
	private String voucherId;
	
	//账簿ID
	private String bookId;
	
	// 批注内容
	private String postil;
	
	/**
	 * 扩充字段  查询显示用
	 */
	private String userId;
	private String loginName;
	private String userName;


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}
	
}
