package com.taoding.common.entity;


import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.domain.user.User;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class DataEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;
	
	// 备注
	@Length(min=0, max=255)
	protected String remarks;	
	
	// 创建者
	@JsonIgnore
	protected User createBy;
	
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	protected Date createDate;	
	
	// 更新者
	@JsonIgnore
	protected User updateBy;
	
	// 更新日期
	@JsonIgnore
	protected Date updateDate;
	
	// 删除标记（0：正常；1：删除；2：审核）
	@JsonIgnore
	@Length(min=1, max=1)
	protected String delFlag; 
	
	
	//所属企业标示
	@JsonIgnore
	protected String enterpriseMarking; 	
	
	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public DataEntity(String id) {
		super(id);
	}

}
