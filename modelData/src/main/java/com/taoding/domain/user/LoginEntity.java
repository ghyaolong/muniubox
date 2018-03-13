package com.taoding.domain.user;

import lombok.Data;

@Data
public class LoginEntity {

	private String loginName;//登陆名称
	
	public LoginEntity() {
	}

	public LoginEntity(String loginName, String password) {
		super();
		this.loginName = loginName;
		this.password = password;
	}

	private String password;//密码
	
	private String validateCode;//验证码
	
	private String companyId;//企业ID
	
	
}
