package com.taoding.common.serviceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taoding.common.utils.StringUtils;
import com.taoding.domain.user.User;



/**
 * 预留处理，用于处理企业标示
 * @author lixc
 * 后期关联配置，是否要添加缓存，自己考虑
 */
@Component
public class EnterpriseUtils {

	/**
	 * 获得当前登录人企业标示
	 * 目前写写死 后期更正
	 * @return
	 */
	public static String getCurrentUserEnter(){
		/*User user=UserUtils.getUser();
		if(null !=user && StringUtils.isNotEmpty(user.getId())){
			return user.getEnterpriseMarking();
		}*/
		return "taodingjituan";
	}
	
	/**
	 * 获得企业编码前缀
	 * @return
	 */
	public static String getCurrentEnterMark(){
		return "TD";
	}
	
	/**
	 * 获得企业基础菜单常量
	 * add lixc
	 * 2017-10-31 11:15:01
	 * @return
	 */
	public static String getBaseMenuId(){
//
//		AtUserEnterpriseConfigure  atUserEnterpriseConfigure=UserUtils.getAtUserEnterpriseConfigure();
//		if(null != atUserEnterpriseConfigure){
//			return atUserEnterpriseConfigure.getBaseMenu();
//		}
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
