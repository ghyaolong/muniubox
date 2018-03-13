package com.taoding.configuration;

public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 是否默认 是1 否 0
	 */
    public static final String NO_DEFAULT ="0";
    public static final String IS_DEFAULT ="1";
	
    
    public static final String START_USERING ="1";//启用
    public static final String FORBIDDEN ="0";//禁用
    
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
	 * 企业是否可用
	 * 0：不可用，1:可用
	 */
	public static final String UNUSEABLE = "0" ;
	public static final String USEABLE = "1";
	
	/**
	 * 用户类型userType
	 * 1：系统超级管理员，2：企业管理员，3：普通用户
	 */
	public static final String ADMIN="1";
	public static final String ATENTERPRISEADMIN="2";
	public static final String USERADMIN="3";

}
