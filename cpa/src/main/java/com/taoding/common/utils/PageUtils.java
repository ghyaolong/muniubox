package com.taoding.common.utils;

import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.taoding.common.serviceUtils.EnterpriseUtils;

public class PageUtils {
	
	public static void page(Map<String,Object> queryMap) {
		
		int pageNo = 1 ;
		int pageSize = 10 ;
		
		if(queryMap.get("pageNo") != null && !"".equals(queryMap.get("pageNo").toString()) 
				&& !"0".equals(queryMap.get("pageNo").toString())){
			pageNo = Integer.valueOf(queryMap.get("pageNo").toString());
		}
		if(queryMap.get("pageSize") != null && !"".equals(queryMap.get("pageSize").toString())
				&& !"0".equals(queryMap.get("pageSize").toString())){
			pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
		}
		
		queryMap.put("enterpriseMarking",EnterpriseUtils.getCurrentUserEnter());
		PageHelper.startPage(pageNo,pageSize);
	}
}
