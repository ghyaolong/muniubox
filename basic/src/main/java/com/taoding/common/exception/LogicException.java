package com.taoding.common.exception;

public class LogicException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String messageInfo;

	public LogicException(String message) {
		super();
		this.messageInfo = message;
	}
	
	public String getMessageInfo( ) {
		return this.messageInfo;
	}

}
