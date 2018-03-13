package com.taoding.service.ticket;

public interface BaseService {
	
	public final static byte FALSE = 0;
	public final static byte TRUE = 1;
	
	/**
	 * 给目标账簿做初始化
	 * @param bookId
	 */
	public void init(String bookId);
	
	/**
	 * 删除所有<br>
	 * 此操作为逻辑删除，此删除提供给票据初始化使用，方便找回数据使用
	 * @param bookId
	 */
	public void deleteAll(String bookId);

}
