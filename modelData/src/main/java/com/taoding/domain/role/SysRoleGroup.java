package com.taoding.domain.role;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.domain.role.Role;
import com.taoding.domain.user.User;

@Data
@ValidationBean
public class SysRoleGroup extends DataEntity<SysRoleGroup> {

	public static final String GROUP ="_group";//用于角色树展示，区分属
	
	private static final long serialVersionUID = 1L;

	@Length(min = 1, max = 100, message = "角色组名称长度必须介于 1 和 100 之间")
	@NotNull(message = "角色组名称不能为空！")
	private String roleGroupName; // 角色组名称

	@NotEmpty(message = "角色组编号不能为空！")
	@Length(min = 0, max = 100, message = "角色组编号长度必须介于 0 和 100 之间")
	private String roleGroupNo; // 角色组编号

	@Length(min = 0, max = 1, message = "状态长度必须介于 0 和 1 之间")
	private String roleGroupStatus; // 状态


	@JsonIgnore
	private List<Role> roleList = new ArrayList<Role>();

	@JsonIgnore
	private User user;

	@JsonIgnore
	private Role role;

	public SysRoleGroup() {
		super();
	}

	public SysRoleGroup(User user) {
		this();
		this.user = user;
	}
}