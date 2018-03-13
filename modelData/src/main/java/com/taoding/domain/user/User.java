package com.taoding.domain.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.taoding.common.annotation.ValidationBean;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExcelField;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.atEnterpriseInfo.AtEnterpriseInfo;
import com.taoding.domain.office.Office;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString
@ValidationBean
public class User extends DataEntity<User> {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Office company;	// 归属公司
	
	@JsonIgnore
	private Office office;	// 归属部门
	
	@ExcelField(title="登录名", align=2, sort=10 ,groups = 1)
	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	@NotNull(message="登陆名不能为空")
	private String loginName;// 登录名 
	@ExcelField(title="密码", align=2, type=2, sort=15,groups = 4)
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间") 
	private String password;// 密码
	@NotNull
	@ExcelField(title="工号", align=2, sort=30,groups = 3)
	@Length(min=1, max=100, message="工号长度必须介于 1 和 100 之间")
	private String no;		// 
	@ExcelField(title="性别", align=2, sort=35,groups = 2)
	private String sex;  //性别
	
	@ExcelField(title="姓名", align=2, sort=40,groups = 1)
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	@NotNull(message="姓名不能为空")
	private String name;	// 姓名
	@ExcelField(title="邮箱", align=2, sort=50,groups = 1)
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	private String email;	// 邮箱
	
	@ExcelField(title="归属部门", align=2, sort=55,groups = 2)
	private String officeNames;  //组织机构
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	private String phone;	// 电话
	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	private String mobile;	// 手机
    
	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
	private String userType;// 用户类型
	
	private String loginIp;	// 最后登陆IP
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;	// 最后登陆日期
	
	private String loginFlag;	// 是否允许登陆
	
	private String photo;	// 头像
	
	private String oldLoginName;// 原登录名
	
	private String newPassword;	// 新密码
	
	private String oldLoginIp;	// 上次登陆IP
	
	private Date oldLoginDate;	// 上次登陆日期
	
	private String queryCondition;//查询条件
	
	private Role role;	// 根据角色查询用户条件
	
	private SysUserExtend sysUserExtend; //用户表扩展
	
	@JsonIgnore
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<Role> roleList = new ArrayList<Role>(); // 拥有角色列表
	@JsonIgnore
	private List<Office> officeList = new ArrayList<Office>(); // 拥有组织机构列表
	/*@JsonIgnore*/
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<String> officeListStr= new ArrayList<String>(); // 拥有组织机构串
	@JsonIgnore
	private List<SysRoleGroup> roleGroupList=new ArrayList<SysRoleGroup>();//角色组
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<AtEnterpriseInfo> enterpriseInfoLists = new ArrayList<AtEnterpriseInfo>();
	
	//公司管理
	private AtEnterpriseInfo atEnterpriseInfo; //公司根据员工的企业标识查找该公司的企业管理员
	
	@ExcelField(title="角色", align=2, sort=45,groups = 2)
	private String roleNames;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;	
	
	/**
	 * 角色选择字符串
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@NotNull(message="不能为空")
	private String roleAiId;
	
 
	public User() {
		super();
		this.loginFlag = Global.YES;
	}
	
	public User(String id){
		super(id);
	}

	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public User(Role role){
		super();
		this.role = role;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}


	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = new ArrayList<String>();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = new ArrayList<Role>();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 获取角色组，角色名称
	 * @return
	 */
	public String getRoleIds() {
		return Collections3.extractToString(roleList, "id", ",");
	}
	
	/**
	 *获得角色角色组关联ID
	 *add lixc
	 *2017-10-27 15:02:34
	 */
	public String getRoleAndRoleGroupIds() {
		StringBuilder sb = new StringBuilder();
		for (SysRoleGroup sysRoleGroup : roleGroupList) {
			sb.append(Collections3.extractToString(roleGroupList, "id", ","));
			sb.append(Collections3.extractToString(sysRoleGroup.getRoleList(), "id", ","));
		}
		return sb.toString();
	}
	
	
	/**
	 *获得角色角色组关联Name
	 *add lixc
	 *2017-10-27 15:02:34
	 */
	public String getRoleNames() {
		StringBuilder sb = new StringBuilder();
		for (SysRoleGroup g : roleGroupList) {
			if (StringUtils.isEmpty(sb)) {
				sb.append(g.getRoleGroupName() + "-").append(Collections3.extractToString(g.getRoleList(), "name", ","));
			} else {
				sb.append("|").append(g.getRoleGroupName() + "-").append(Collections3.extractToString(g.getRoleList(), "name", ","));
			}
		}
		return sb.toString();
	}
	/**
	 * add lixc 
	 * 2017年10月26日10:20:06
	 * 用户一个用户多个部门
	 * @return
	 */
	public String getOfficeIds(){
		return Collections3.extractToString(officeList, "id", ",");
	}
	
	public String getOfficeNames(){
		return Collections3.extractToString(officeList, "name", ",");
	}

	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	 
	@ExcelField(title="添加日期", align=1, type=1, sort=60,groups = 3)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="备注", align=1,type=1, sort=70,groups = 3)
	public String getRemarks() {
		return remarks;
	}
	
}