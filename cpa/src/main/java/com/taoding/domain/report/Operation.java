package com.taoding.domain.report;

/**
 * 操作符的枚举
 * @author Yang Ji Qiang
 *
 */
public enum Operation {
	
	//加
	ADD(1),
	
	//减
	MINUS(-1);

	
	private int value;
	
	private Operation(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
}
