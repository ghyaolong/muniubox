package com.taoding.domain.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.entity.DataEntity;

public class SysUserExtend extends DataEntity<SysUserExtend> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// user_id
	private String sex;		// 性别
	private String yuliu1;		// yuliu1
	private String yuliu2;		// yuliu2
	
	public SysUserExtend() {
		super();
	}

	public SysUserExtend(String id){
		super(id);
	}

	@NotNull(message="user_id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=1, message="性别长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=64, message="yuliu1长度必须介于 0 和 64 之间")
	public String getYuliu1() {
		return yuliu1;
	}

	public void setYuliu1(String yuliu1) {
		this.yuliu1 = yuliu1;
	}
	
	@Length(min=0, max=64, message="yuliu2长度必须介于 0 和 64 之间")
	public String getYuliu2() {
		return yuliu2;
	}

	public void setYuliu2(String yuliu2) {
		this.yuliu2 = yuliu2;
	}
	
}