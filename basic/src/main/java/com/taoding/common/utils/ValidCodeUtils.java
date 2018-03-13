package com.taoding.common.utils;

import org.apache.commons.lang3.StringUtils;

/**   
 * @ClassName:  ValidCodeServiceImpl   
 * @Description:获取手机验证码，将来需要将关键性的逻辑替换成短信平台接口
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月13日 上午11:00:08   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class ValidCodeUtils {
	
	/**   
	 * @Title: getValidCode   
	 * @Description: 获取手机验证码
	 * @param: @param mobile
	 * @param: @return      
	 * @return: String      
	 * @throws   
	 */  
	public static String getValidCode(String mobile) {
		if(StringUtils.isNotEmpty(mobile)) {
			// XXX 调用短信接口平台,目前只是生成随机数，没有将随机数已短信的形式发送出去，以后需要接入短信网关
			String validCode =  String.valueOf(ValidCodeUtils.getRandom());
			//CacheMapUtils cacheMapUtils = CacheMapUtils.getInstance();
			CacheMapUtils.add(mobile, validCode, 30);
			return validCode;
		}
		return null;
	}
	
	/**   
	 * @Title: getRandom   
	 * @Description: 获取随机数，默认为6位
	 * @param: @return      
	 * @return: int      
	 * @throws   
	 */  
	private static int getRandom() {
		return (int)((Math.random()*9+1)*100000);
	}
	/**   
	 * @Title: getRandom   
	 * @Description: 获取随机数
	 * @param:length 随机数长度
	 * @return: int      
	 * @throws   
	 */  
	private static int getRandom(int length) {
		String baseStr = "1";
		for (int i = 1; i < length; i++) {
			baseStr = baseStr+"0";
		}
		int base = Integer.parseInt(baseStr);
		return (int)((Math.random()*9+1)*base);
	}
	
	public static void main(String[] args) {
		System.out.println(ValidCodeUtils.getRandom(10));
	}
}
