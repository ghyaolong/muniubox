package com.taoding.domain.initialize;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class InitEntity {
	@NotNull(message = "用户名为空")
	private String loginName; // 用户名
	@NotNull(message = "密码为空")
	private String passWord; // 密码
	@NotNull(message = "账薄信息为空")
	private String accountingBookId; // 账薄id
	private boolean bookType; // 账套 初始化标示 否(false) 是(true)
	private boolean ticketType; // 票据初始化标示否(false) 是(true)
	private boolean payType; // 薪酬初始化标示否(false) 是(true)

}
