package com.taoding.common.exception;

/**   
 * @ClassName:  BRecognition   
 * @Description:百度OCR服务解析图片异常类
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月7日 上午10:05:16   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class BRecognitionException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String messageInfo;

	public BRecognitionException(String message) {
		super();
		this.messageInfo = message;
	}
	
	public BRecognitionException(String message,Throwable t) {
		super(message,t);
		this.messageInfo = message;
	}
	
	
	public String getMessageInfo( ) {
		return this.messageInfo;
	}
}
