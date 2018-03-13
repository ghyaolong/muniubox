package com.taoding.domain.menu;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ValidationBean
public class Menu extends DataEntity<Menu> {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Menu parent;	// 父级菜单
	
	@Length(min=1, max=2000)
	private String parentIds; // 所有父级编号
	
	@Length(min=1, max=100)
	private String name; 	// 名称
	
	@Length(min=0, max=2000)
	private String href; 	// 链接
	
	@Length(min=0, max=20)
	private String target; 	// 目标（ mainFrame、_blank、_self、_parent、_top）
	
	@Length(min=0, max=100)
	private String icon; 	// 图标
	
	@NotNull
	private Integer sort; 	// 排序
	
	@Length(min=1, max=1)
	private String isShow; 	// 是否在菜单中显示（1：显示；0：不显示）
	
	@NotNull
	private String platform; //所属模块
	
	@Length(min=0, max=200)
	private String permission; // 权限标识
	
	@JsonIgnore
	private String userId;
	
	//基础菜单业务
	private String enterpriseBusinessId;
	
	//子菜单
	private List<Menu> subMenu;
	
	public Menu(){
		super();
		this.sort = 30;
		this.isShow = "1";
	}
	
	public Menu(String id){
		super(id);
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
	
	@JsonIgnore
	public static String getRootId(){
		return "1";
	}
 
	@Override
	public String toString() {
		return "[name:" + name + "; url:" + this.href + "]";
	}
}