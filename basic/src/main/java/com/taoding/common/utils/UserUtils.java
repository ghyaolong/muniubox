package com.taoding.common.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.taoding.common.shiro.JwtUserInfo;
import com.taoding.common.utils.Encodes;

/**
 * 用户工具类
 */
public class UserUtils {

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	
	public static final String CACHE_AUTH_INFO = "authInfo";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";
	
	/**
	 * 企业配置
	 */
	public static final String CACHE_ENTER_CONFIG_="enterConfig_";
	
	
	/**
     * 验证密码
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Encodes.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, 1024);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }
    
    /**
	 * 获取当前用户的登录名
	 * @return
	 */
	public static String getCurrentLoginName() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getLoginName();
	}
	
	/**
	 * 获取当前用户的用户名
	 * @return
	 */
	public static String getCurrentUserName() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getUserName();
	}
	
	/**
	 * 获取当前用户的用户Id
	 * @return
	 */
	public static String getCurrentUserId() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getUserId();
	}
	
	/**
	 * 获取当前用户的角色id列表
	 * @return
	 */
	public static List<String> getCurrentUserRoles() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getRoles();
	}
	
	/**
	 * 获取当前用户所属的企业id
	 * @return
	 */
	public static String getCurrentUserEnterpriseId() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getEnterpriseId();
	}
}
