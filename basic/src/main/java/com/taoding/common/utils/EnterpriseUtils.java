package com.taoding.common.utils;

import org.springframework.stereotype.Component;

/**
 * 预留处理，用于处理企业标示
 * @author lixc
 * 后期关联配置，是否要添加缓存，自己考虑
 */
@Component
public class EnterpriseUtils {

	/**
	 * 获得用户当前的所隶属的企业ID
	 * 目前写写死 后期更正
	 * @return
	 */
	public static String getCurrentUserEnter(){
		if(StringUtils.isNotEmpty(UserUtils.getCurrentUserEnterpriseId())){
			return UserUtils.getCurrentUserEnterpriseId();
		} 
		return "taodingjituan";
	}
	
	/**
	 * 获得企业编码前缀，每个企业可能有自己不同的编码
	 * @return
	 */
	public static String getCurrentEnterMark(){
		return "TD";
	}
	
	/**
	 * 获得企业菜单的模板项标识
	 * add lixc
	 * 2017-10-31 11:15:01
	 * @return
	 */
	public static String getBaseMenuId(){
		return "taodingjituanBaseMenuId";
 
	}
	
	/**
	 * 获得企业编码前缀
	 * @return
	 */
	public static String getEnterpriseTemplate(){
		return "taodingjituan";
	}
}
