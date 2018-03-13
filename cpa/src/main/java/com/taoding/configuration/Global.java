package com.taoding.configuration;

public class Global {

	
	
	
	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	
	/**
	 * 保留下属点位数
	 */
	public static final int KEEP_DIGITS =2;
	public static final int KEEP_DIGITS_THREE =3;

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final int YES_INT =1;
	public static final int NO_INT = 0;
	
	
	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	
	/**
	 * 对/错
	 */
	public static final int TRUE_1 = 1;
	public static final int FALSE_0 = 0;
	
	
	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";
	
	/**
	 * 	日期格式
	 */
	public static final String DATA_FORMATE="yyyy-MM-dd";
	
	/**
	 * 固定资产编号
	 */
	public static final String FIXED_ASSET_NO = "1601";
	
	/**
	 * 固定资产清理编号
	 */
	public static final String FIXED_ASSET_CLEAR_NO = "1606";
	
	
	
	/**
	 * 无形资产编号
	 */
	public static final String INTANGIBLE_ASSET_NO = "1701";
	
	/**
	 * 累计摊销编号
	 */
	public static final String INTANGIBLE_ASSET_AMORTIZE_NO = "1702";
	/**
	 * 长期待摊费用编号 
	 */
	public static final String LONG_APPORTIONED_ASSET_NO = "1801";
	
	
	public static final String TAX_AND_ADDITIONAL="5403";//计提税金及附加
	
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
}
