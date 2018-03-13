package com.taoding.service.role;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.User;
import com.taoding.mapper.role.RoleDao;
import com.taoding.mapper.user.UserDao;


/**
 * 角色组管理Service
 * @author lixc
 * @version 2017-09-29
 */
@Service
@Transactional(readOnly=true)
public class RoleServiceImpl extends DefaultCurdServiceImpl<RoleDao, Role> implements RoleService {
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public String findRoleNo(){
		String mark=EnterpriseUtils.getCurrentEnterMark();
		Role role = new Role();
		role.setEnname(mark);
		String markStr=dao.findMaxRoleNo(role);
		if(StringUtils.isBlank(markStr)||StringUtils.isEmpty(markStr)){
			markStr =mark+"0000001";
		}else{
			markStr=markStr.replace(mark, "");
			int numMark=Integer.parseInt(markStr)+1;
 			markStr=mark+String.format("%07d", numMark);
		}
		return markStr;
	}
	
	@Override
	public boolean checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return true;
		} else if (name!=null) {
			Role r = new Role();
			r.setName(name);
			r=dao.getByName(r);
			if(null!=r){
				return true;
			}
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (null == role)
			throw new LogicException("角色不能为空!");
		
		//处理角色组
		if(StringUtils.isNotEmpty(role.getRoleGroupId())){
			SysRoleGroup sysRoleGroup = new SysRoleGroup();
			sysRoleGroup.setId(role.getRoleGroupId());
			role.setSysRoleGroup(sysRoleGroup);
		}else if(null==role.getSysRoleGroup()){
			throw new LogicException("用户组信息不能为空！请关联用户组！");
		}
		
		 
		// 禁用验证是否有关联用户
		if ("0".equals(role.getUseable())) {
			User userTemp = new User();
			userTemp.setRole(role);
			long count = userDao.findUserRoleCount(userTemp);
			if (count > 0) {
				throw new LogicException("请先移除'" + role.getName() + "'下的用户！");
			}
			// 默认生成的不能禁用
			if (StringUtils.isNotBlank(role.getId())) {
				role = dao.get(role.getId());
				if ("1".equals(role.getIsDefault())) {
					throw new LogicException("角色'" + role.getName()
							+ "'为基础角色，不能禁用！");
				}
			}
		}

		if (StringUtils.isEmpty(role.getId())) {// 添加角色时重新获得角色编号
			String strRoleNo = findRoleNo();
			role.setEnname(strRoleNo);
		}
		super.save(role);
	}
	@Override
	@Transactional(readOnly = false)
	public void saveRoleMenu(String menuIds,String roleId){
		if(StringUtils.isBlank(menuIds)||StringUtils.isBlank(roleId)){
			throw new LogicException("角色id或菜单Id不能为空!");
		}
		Role role = new Role();
		role.setId(roleId);
		
		List<String> menuList = Arrays.asList(menuIds.split(","));
		dao.deleteRoleMenu(role);
		if (!CollectionUtils.isEmpty(menuList)){
			dao.insertRoleMenu(roleId,menuList);
		}
	}
	
	
	/**
	 * 移除角色用户关联
	 * @param ids
	 * @param roleId 移除角色Id
	 * add lixc
	 * 2017年10月9日19:49:57
	 */
	@Transactional(readOnly = false)
	@Override
	public void deleteBatchRemove(String ids,String roleId){
		User user = new User();
		user.setRole(new Role("roleId"));
		userDao.deleteUserRole(user);
	}
	
	/**
	 * 角色用户关联
	 * @param ids
	 * @param roleId 移除角色Id
	 * add lixc
	 * 2017-10-10 18:51:04
	 */
	@Transactional(readOnly = false)
	public void insertBatchJson(String ids,String roleId){
	 if(StringUtils.isBlank(ids)||StringUtils.isBlank(roleId)){
		 throw new LogicException("参数不能为空！");
	 }
		userDao.insertUserRoleForRoleGroup(roleId, Arrays.asList(ids.split(",")));
	}
}