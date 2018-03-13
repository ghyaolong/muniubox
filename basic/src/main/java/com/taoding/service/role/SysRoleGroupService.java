package com.taoding.service.role;

import java.util.List;

import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.CrudService;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.User;
import com.taoding.mapper.role.SysRoleGroupDao;

/**
 * 角色组管理Service
 * @author lixc
 * @version 2017-09-29
 */
public interface SysRoleGroupService extends CrudService<SysRoleGroupDao, SysRoleGroup> {
	
	/**
	 * 按条件获得角色树
	 * @param queryMap
	 * @return
	 */
	public TreeNode findSysGroupTree(SysRoleGroup sysRoleGroup);

	/**
	 * 获得角色组编号
	 * @return
	 * @author lixc
	 * @date 2017年11月10日18:28:41
	 */
	public String  getGroupNo();
	
	
	/**
	 * 查询角色组下角色的数量
	 * @param role
	 * @return
	 * @author lixc
	 * @date 2017-11-11 10:16:34
	 */
	public long findRoleCount(Role role);
	
	/**
	  * 检查角色组下用角色是否存在
	  * @param id
	  * @param operation 1 删除  2禁用
	  * @author lixc
	  * @date 2017-11-11 10:07:24
	  */
	public boolean checkRoleGroup(String id,String operationType);
 
	public void saveEnterpriseTemplateSysRoleGroupList(List<SysRoleGroup> roleGroupList, User user,String enterpriseTemplate,String strId);
	/**
	 * 插入模板角色数据
	 * @param roleList
	 * @param user
	 * @param enterpriseTemplate
	 * @param strId
	 * @return
	 * mhb
	 * 2017.10.30 20:39
	 */
	public void saveEnterpriseTemplateSysRoleList(List<Role> roleList,User user,
			String enterpriseTemplate,String strId);

	public void saveEnterpriseTemplateSysUserRoleList(List<Role> roleList);
	/**
	 * 插入模板用户角色组数据
	 * @param roleGroupList
	 * @return
	 * mhb
	 * 2017.10.30 20:39
	 */
	public void saveEnterpriseTemplateSysUserRoleGroupList( List<SysRoleGroup> roleGroupList);
}