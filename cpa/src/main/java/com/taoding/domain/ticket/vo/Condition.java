package com.taoding.domain.ticket.vo;

import lombok.Data;

/**
 * 票据检验条件
 * @author 刘鑫
 *
 */
@Data
public class Condition {
	private Byte isVoid;	// 是否无效
	private Byte completed;	// 是否已做账
	private Byte isLllegal;	// 是否是问题票据
	private Byte isIdentify;// 是否已识别
	
	/**
	 * 
	 * @param isVoid 是否作废
	 * @param completed 是否已做账
	 * @param isLllegal 是否问题票据
	 * @param isIdentify 是否已识别
	 */
	public Condition(Boolean isVoid, Boolean completed, Boolean isLllegal, Boolean isIdentify) {
		this.isVoid = isVoid == null ? null : (byte) (isVoid ? 1 : 0);
		this.completed = completed == null ? null : (byte) (completed ? 1 : 0);
		this.isLllegal = isLllegal == null ? null : (byte) (isLllegal ? 1 : 0);
		this.isIdentify = isIdentify == null ? null : (byte) (isIdentify ? 1 : 0);
	}
}
