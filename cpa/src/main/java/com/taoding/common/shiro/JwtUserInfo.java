package com.taoding.common.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * 从jwt中提取的用户信息
 * @author vincent
 *
 */
@Getter
public class JwtUserInfo {
	
	/**
	 * 用户名
	 */
	
	private final String userName;
	
	/**
	 * 登录名
	 */
	private final String loginName;
	
	/**
	 * 角色名
	 */
	private final List<String> roles;
	
	/**
	 * token
	 */
	private final String token;
	
	/**
	 * userId
	 */
	private final String userId;
	
	/**
	 * 用户访问的IP地址
	 */
	private final String userRemoteIp;
	
	/**
	 * 储存其它信息
	 */
	private Map<String, Object> otherInfo = new HashMap<>();
	
	/**
	 * 所属企业
	 */
	private final String enterpriseId;
	
	public static class Builder {
		private String userName = "";
		private String loginName = "";
		private List<String> roles = new ArrayList<>();
		private String userId = null;
		private String enterpriseId = null;
		private String token;
		private String userRemoteIp;
		
		public Builder() {
		}
		
		public Builder userName(String userName) {
			this.userName = userName;
			return this;
		}
		
		public Builder loginName(String loginName) {
			this.loginName = loginName;
			return this;
		}
		
		public Builder role(List<String> roles) {
			this.roles = roles;
			return this;
		}
		
		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}
		
		public Builder enterpriseId(String enterpriseId) {
			this.enterpriseId = enterpriseId;
			return this;
		}
		
		public Builder token(String token) {
			this.token = token;
			return this;
		}
		
		public Builder userRemoteIp(String userRemoteIp) {
			this.userRemoteIp = userRemoteIp;
			return this;
		}
		
		public JwtUserInfo build() {
			return new JwtUserInfo(this);
		}
	}
	
	private JwtUserInfo(Builder builder) {
		this.userName = builder.userName;
		this.loginName = builder.loginName;
		this.roles = builder.roles;
		this.userId = builder.userId;
		this.enterpriseId = builder.enterpriseId;
		this.token = builder.token;
		this.userRemoteIp = builder.userRemoteIp;
	}
	
	public void setInfo(String key, Object obj) {
		otherInfo.put(key, obj);
	}
	
	public Object getInfo(String key) {
		return otherInfo.get(key);
	}
}
