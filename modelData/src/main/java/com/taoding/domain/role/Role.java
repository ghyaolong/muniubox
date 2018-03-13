package com.taoding.domain.role;
 

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.taoding.common.entity.DataEntity;
import com.taoding.configuration.Global;
import com.taoding.domain.menu.Menu;
import com.taoding.domain.office.Office;
import com.taoding.domain.user.User;

@Data
public class Role extends DataEntity<Role> {
	
	public  static final String ROLE = "_role";
	
	private static final long serialVersionUID = 1L;
	private Office office;	// 归属机构
	@Length(min=1, max=100)
	@NotEmpty(message="角色名称不能为空！")
	private String name; 	// 角色名称
	
	@Length(min=1, max=100)
	@NotEmpty(message="角色编号不能为空！")
	private String enname;	// 英文名称
	
	@Length(min=1, max=100)
	private String roleType;// 权限类型
	
	private String dataScope;// 数据范围
	
	private String oldName; 	// 原角色名称
	
	private String oldEnname;	// 原英文名称
	
	private String sysData ="1"; 		//是否是系统数据 默认为可用
	
	private String useable; 		//是否是可用

	@JsonIgnore
	private SysRoleGroup sysRoleGroup;//用户组
	
	@JsonIgnore
	private User user;		// 根据用户ID查询角色列表
	
	@NotEmpty(message="角色组不能为空！")
	private String roleGroupId; 
	
	private String isDefault;  //是否为默认角色
	@JsonIgnore
	private List<Menu> menuList = new ArrayList<Menu>();// 拥有菜单列表
	@JsonIgnore
	private List<Office> officeList =new ArrayList<Office>(); // 按明细设置数据范围

	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	public static final String DATA_SCOPE_COMPANY = "3";
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	public static final String DATA_SCOPE_OFFICE = "5";
	public static final String DATA_SCOPE_SELF = "8";
	public static final String DATA_SCOPE_CUSTOM = "9";
	
	public Role() {
		super();
		this.dataScope = DATA_SCOPE_OFFICE_AND_CHILD;
		this.useable=Global.YES;
	}
	
	public Role(String id){
		super(id);
	}
	
	public Role(User user) {
		this();
		this.user = user;
	}
	@JsonIgnore
	public List<String> getMenuIdList() {
		List<String> menuIdList = new ArrayList<String>();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = new ArrayList<Menu>();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}
	
	public void setMenuIds(String menuIds) {
		menuList = new ArrayList<Menu>();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Arrays.asList(ids));
		}
	}
	
	@JsonIgnore
	public List<String> getOfficeIdList() {
		List<String> officeIdList =new ArrayList<String>();
		for (Office office : officeList) {
			officeIdList.add(office.getId());
		}
		return officeIdList;
	}

	public void setOfficeIdList(List<String> officeIdList) {
		officeList = new ArrayList<Office>();
		for (String officeId : officeIdList) {
			Office office = new Office();
			office.setId(officeId);
			officeList.add(office);
		}
	}

	public String getOfficeIds() {
		return StringUtils.join(getOfficeIdList(), ",");
	}
	
	public void setOfficeIds(String officeIds) {
		officeList =  new ArrayList<Office>();
		if (officeIds != null){
			String[] ids = StringUtils.split(officeIds, ",");
			setOfficeIdList(Arrays.asList(ids));
		}
	}
	
	/**
	 * 获取权限字符串列表
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	public String getMenuNames() {
		List<String> menuNameList = Lists.newArrayList();
		for (Menu menu : menuList) {
			menuNameList.add(menu.getName());
		}
		return StringUtils.join(menuNameList, ",");
	}
	
}
