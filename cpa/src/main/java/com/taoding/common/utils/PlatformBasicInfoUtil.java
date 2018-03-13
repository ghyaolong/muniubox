package com.taoding.common.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.taoding.domain.menu.Menu;

/**
 * 用来请求基础平台上的基本信息
 * @author vincent
 *
 */
@Component
public class PlatformBasicInfoUtil{
	
//	private static final Logger logger = LoggerFactory.getLogger(PlatformBasicInfoUtil.class);
	
	@Value("${platform.url}")
	private String platformAddress;
    
    @SuppressWarnings("unchecked")
    public List<Menu> getMenuByUserId(String userId, String token) {
		List<Menu> menuList = (List<Menu>) HttpClientUtil.sendGet(platformAddress + "/menu/userMenu/" + userId, token, List.class, Menu.class);
    	return menuList;
    }
}
