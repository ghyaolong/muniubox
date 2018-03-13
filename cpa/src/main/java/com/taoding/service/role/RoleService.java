package com.taoding.service.role;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.service.CrudService;
import com.taoding.domain.role.Role;
import com.taoding.mapper.role.RoleDao;

/**
 * 角色组管理Service
 * @author lixc
 * @version 2017-09-29
 */
public interface RoleService extends CrudService<RoleDao, Role> {
	/**
	 * 获得角色组编号
	 * @return
	 * @author lixc
	 * @date 2017年11月11日11:03:16
	 */
	public String findRoleNo();
	
	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 * @author lixc
	 * @date 2017-11-11 11:59:03
	 */
	public boolean checkName(String oldName, String name);

	/**
	 * 保存角色
	 * @param role
	 * @author lixc
	 * @date 2017年11月11日13:43:25
	 */
	public void saveRole(Role role);
	/**
	 * 保存角色菜单关联
	 * @param menuIds
	 * @param roleId
	 * @author lixc
	 * @date 2017年11月11日18:10:41
	 */
	public void saveRoleMenu(String menuIds,String roleId);
	
	/**
	 * 移除用户角色关联
	 * @param ids 用户ID 串
	 * @param roleId
	 * @author lixc
	 * @date 2017-10-10 18:51:04
	 */
	public void deleteBatchRemove(String ids,String roleId);
	
	/**
	 * 移除角色用户关联
	 * @param ids
	 * @param roleId 移除角色Id
	 * @author lixc
	 * @date 2017-10-10 18:51:04
	 */
	@Transactional(readOnly = false)
	public void insertBatchJson(String ids,String roleId);
}


