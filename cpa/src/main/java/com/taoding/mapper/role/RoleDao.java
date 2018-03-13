package com.taoding.mapper.role;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.taoding.common.dao.CrudDao;
import com.taoding.domain.menu.Menu;
import com.taoding.domain.role.Role;

@Repository
@Mapper
public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * 维护角色与菜单权限关系
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(@Param("roleId")String roleId,@Param("menuList")List<String> menuList);
	
	/**
	 * 维护角色与公司部门关系
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);
	
	/**
	 * 批量部分修改角色
	 * add lixc 
	 * 2017年10月11日18:01:40
	 */
	public int updateBatch(List<Role> roleList);
	
	/**
	 * 查询角色数量
	 * @param role
	 * @return
	 */
	public long findRoleCount(Role role);
	
	/**
	 * 获得最大角色编号
	 * @param role
	 * @return
	 */
	public String findMaxRoleNo(Role role);
	/**
	 * 查询模板角色
	 * @param role
	 * @return
	 * mhb
	 */
	public List<Role> findEnterpriseTemplateForRole(Role role);
	/**
	 * 插入模板角色
	 * @param roleList
	 * @return
	 * mhb
	 */

	public void insertEnterpriseTemplateSysRoleList(@Param("roleEnterpriseTemplateList")List<Role> roleList);
	/**
	 * 插入模板用户角色关系
	 * @param roleList
	 * @return
	 * mhb
	 */

	public void insertEnterpriseTemplateSysUserRoleList(@Param("roleList") List<Role> roleList);
	               
}
