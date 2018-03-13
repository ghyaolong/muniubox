package com.taoding.common.utils;

import java.util.HashMap;
import java.util.Map;

/**   
 * @ClassName:  AreaDict   
 * @Description:地区数字字典，根据增值税发票的前4位确定发票的所在地
 * @author: yaochenglong@taoding.cn 
 * @date:   2017年12月14日 下午4:29:26   
 *     
 * @Copyright: 2017 www.taoding.cn Inc. All rights reserved. 
 */
public class AreaDictUtils {
	
	static Map<String,String> map = null;
	
	static {
		map = new HashMap<String,String>();
		map.put("1100","北京市");
		map.put("6100", "陕西省");
		map.put("4400", "广东省");
	}
	
	/**
	 *根据areaCode获取对应的省份名称,areaCode位4位数字<br/>
	 *areaCode的四位数字来自于  
	 *
	 */
	public static String getProvinceByCode(String areaCode) {
		return map.get(areaCode);
	}
	
	public static void main(String[] args) {
		System.out.println(AreaDictUtils.getProvinceByCode("6100"));
	}

}
