package com.taoding.domain.office;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.taoding.common.entity.TreeEntity;
import com.taoding.domain.area.Area;
import com.taoding.domain.user.User;

import lombok.Data;

@Data
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
	
	// 父级编号
//	private Office parent;	
	
	// 所有父级编号
//	private String parentIds; 
	
	// 归属区域
	@NotNull
	private Area area;
	
	// 机构编码
	@Length(min=0, max=100)
	private String code;
	
	// 机构名称
//	private String name;
	
	// 排序
//	private Integer sort;
	
	// 机构类型（1：公司；2：部门；3：小组）
	@Length(min=1, max=1)
	private String type;
	
	// 机构等级（1：一级；2：二级；3：三级；4：四级）
	@Length(min=1, max=1)
	private String grade;
	
	// 联系地址
	@Length(min=0, max=255)
	private String address;
	
	// 邮政编码
	@Length(min=0, max=100)
	private String zipCode;
	
	// 负责人
	@Length(min=0, max=100)
	private String master;
	
	// 电话
	@Length(min=0, max=200)
	private String phone;
	
	// 传真
	@Length(min=0, max=200)
	private String fax;
	
	// 邮箱
	@Length(min=0, max=200)
	private String email;
	
	//是否可用
	private String useable;
	
	//主负责人
	private User primaryPerson;
	
	//副负责人
	private User deputyPerson;
	
	//快速添加子部门
	private List<String> childDeptList;
	
	//是否默认
	private String isDefault;

	//来源
	private String source;
	
	//备注
	private String remarks;
	

	public Office(){
		super();
//		this.sort = 30;
		this.type = "2";
	}

	public Office(String id){
		super(id);
	}
	
//	@JsonBackReference
//	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return name;
	}


}