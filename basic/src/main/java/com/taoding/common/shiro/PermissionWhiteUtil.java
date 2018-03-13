package com.taoding.common.shiro;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 存储公用的，只要是登录大家都可以访问的内容
 * @author vincent
 *
 */
public class PermissionWhiteUtil {
	
	private final static Set<String> whiteList = new HashSet<>();
	
	static {
		whiteList.add("/menu/userMenu/{id}");
	}
	
	public static boolean isInWhiteList(String permision) {
		boolean isPermit = false;
		for (String permistionStr : whiteList) {
			String replacedPattern = permistionStr.replace("{id}", "[a-zA-Z0-9]+").toLowerCase();
    		Pattern p = Pattern.compile(replacedPattern);
    		Matcher m = p.matcher(permision.toLowerCase());
    		if (m.matches()) {
    			isPermit = true;
    			break;
    		}
		}
		return isPermit;
	}

}
