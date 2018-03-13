package com.taoding.common.utils;

import java.util.List;

import org.apache.shiro.SecurityUtils;

import com.taoding.common.shiro.JwtUserInfo;

/**
 * 获取当前用户相关信息的工具类
 * @author vincent
 *
 */
public class UserUtils {
	
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
	
	/**
	 * 获取当前的用户所使用的token字符串
	 * @return
	 */
	public static String getCurrentUserToken() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getToken();
	}
	
	/**
	 * 获取用户访问的ip地址
	 * @return
	 */
	public static String getCurrentUserRemoteAddress() {
		JwtUserInfo userInfo = (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
		return userInfo.getUserRemoteIp();
	}
	
	/**
	 * 获取当前操作的用户的信息
	 * @return
	 */
	public static JwtUserInfo getCurrentUserInfo() {
		return (JwtUserInfo)SecurityUtils.getSubject().getPrincipal();
	}

}
