package com.taoding.domain.user;

import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.entity.DataEntity;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;

/**
 * 企业，组织，用户管理Entity
 * 
 * @author lixc
 * @version 2017-10-26
 */
@Data
public class SysEnterpriseUser extends DataEntity<SysEnterpriseUser> {

	private static final long serialVersionUID = 1L;
	private User user; // 用户ID
	@Length(min = 1, max = 64, message = "企业ID/企业标示长度必须介于 1 和 64 之间")
	private String enterpriseId; // 企业ID/企业标示
	@Length(min = 1, max = 1, message = "默认选中，用于常用企业长度必须介于 1 和 1 之间")
	private String defualtValue; // 默认选中，用于常用企业
	private Office office; // 归属部门

	public SysEnterpriseUser() {
		super();
	}

	public SysEnterpriseUser(String id) {
		super(id);
	}
}