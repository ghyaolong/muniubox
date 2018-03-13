package com.taoding.mapper.role;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.taoding.common.dao.CrudDao;
import com.taoding.domain.role.SysRoleGroup;


/**
 * 角色组管理DAO接口
 * @author lixc
 * @version 2017-09-29
 */
@Repository
@Mapper
public interface SysRoleGroupDao extends CrudDao<SysRoleGroup> {
	
	public List<Map<String, String>> findSysGroupTree(Map<String, Object> queryMap);
  
	/**
	 * 获得最大的角色组编号
	 * @param sysRoleGroup
	 * @return
	 */
	public String findMaxGroupNo(SysRoleGroup sysRoleGroup);

	public List<SysRoleGroup> findRoleGroupList(SysRoleGroup sysRoleGroup);
	
	/**
	 * 获得角色组关联角色
	 * @param sysRoleGroup
	 * @return
	 *  
	 */
    public List<SysRoleGroup>  selectSysGroupRole(SysRoleGroup sysRoleGroup);
    /**
	 * 查询模板角色组
	 * @param sysRoleGroup
	 * @return
	 * mhb
	 *  
	 */
	public List<SysRoleGroup> findEnterpriseTemplateForRoleGroup( SysRoleGroup sysRoleGroupEnterpriseTemplateGroup);
	 /**
		 *插入模板角色组关系
		 * @param roleGroupList
		 * @return
		 * mhb
		 *  
		 */
	public void insertEnterpriseTemplateForRoleGroup(@Param("roleGroupList") List<SysRoleGroup> roleGroupList);
	 /**
	 *插入模板用户角色组关系
	 * @param roleGroupList
	 * @return
	 * mhb
	 *  
	 */
	public void insertEnterpriseTemplateSysUserRoleGroupList(@Param("roleGroupList") List<SysRoleGroup> roleGroupList);
 
}