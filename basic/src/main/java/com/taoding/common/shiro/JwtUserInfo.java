package com.taoding.common.shiro;

import java.util.ArrayList;
import java.util.List;

/**
 * 从jwt中提取的用户信息
 * @author vincent
 *
 */
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
	 * userId
	 */
	private final String userId;
	
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
	}

	public String getUserName() {
		return userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getUserId() {
		return userId;
	}

	public String getEnterpriseId() {
		return enterpriseId;
	}
	
}
