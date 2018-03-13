package com.taoding.common.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JWTAuthenticationToken，用来代替shiro的UsernameAndPasswordToken
 * @author vincent
 *
 */
public class JwtShiroAuthenticationToken implements AuthenticationToken{
	
	private static final long serialVersionUID = 5559598788616981742L;

	
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
	
	/**
	 * JWT Token
	 */
	private final String jwtToken;
	

	@Override
	public Object getPrincipal() {
		return new JwtUserInfo.Builder()
				.userName(this.userName)
				.loginName(this.loginName)
				.role(this.roles)
				.userId(this.userId)
				.enterpriseId(this.enterpriseId)
				.build();
	}

	@Override
	public Object getCredentials() {
		return this.jwtToken;
	}
	
	public static class Builder{
		
		private String userName;
		
		private String loginName;
		
		private List<String> roles;
		
		private String userId;
		
		private String enterpriseId;
		
		private String jwtToken;
		
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
		
		public Builder roles(List<String> roles) {
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
		
		public Builder jwtToken(String jwtToken) {
			this.jwtToken = jwtToken;
			return this;
		}
		
		public JwtShiroAuthenticationToken build() {
			return new JwtShiroAuthenticationToken(this);
		}
	}
	
	private JwtShiroAuthenticationToken(Builder builder) {
		this.userName = builder.userName;
		this.loginName = builder.loginName;
		this.roles = builder.roles;
		this.userId = builder.userId;
		this.enterpriseId = builder.enterpriseId;
		this.jwtToken = builder.jwtToken;
	}
}
