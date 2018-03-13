package com.taoding.service.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.exception.LogicException;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.User;
import com.taoding.mapper.role.RoleDao;
import com.taoding.mapper.role.SysRoleGroupDao;
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
	
	@Autowired
	private SysRoleGroupDao sysRoleGroupDao;
	
	@Override
	public String findRoleNo(){
		String mark=EnterpriseUtils.getCurrentEnterMark();
		Role role = new Role();
		role.setEnname(mark);
		role.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
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
			SysRoleGroup sysRoleGroup =sysRoleGroupDao.get(role.getRoleGroupId());
			if(null !=sysRoleGroup ){
				sysRoleGroup.setId(role.getRoleGroupId());
				role.setSysRoleGroup(sysRoleGroup);
			}
		}
		
		if(null==role.getSysRoleGroup()){
			throw new LogicException("角色组信息不能为空！请关联角色组！");
		}
		
		 
		//编辑时 禁用验证是否有关联用户
		if ("0".equals(role.getUseable()) && StringUtils.isNotBlank(role.getId())) {
			User userTemp = new User();
			userTemp.setRole(role);
			long count = userDao.findUserRoleCount(userTemp);
			if (count > 0) {
				throw new LogicException("请先移除'" + role.getName() + "'下的用户！");
			}
			// 默认生成的不能禁用
			if (StringUtils.isNotBlank(role.getId())) {
				role = dao.get(role.getId());
				if (Global.IS_DEFAULT.equals(role.getIsDefault())) {
					throw new LogicException("角色'" + role.getName()
							+ "'为基础角色，不能禁用！");
				}
			}
		}

		if (StringUtils.isEmpty(role.getId())) {// 添加角色时重新获得角色编号
			String strRoleNo = findRoleNo();
			role.setEnname(strRoleNo);
		}
		if(StringUtils.isBlank(role.getEnterpriseMarking())){
			role.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		}
		super.save(role);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteRole(String id){
		Role role =get(id);
//    	role.setId(id);
    	User userTemp = new User();
		userTemp.setRole(role);
		long count = userDao.findUserRoleCount(userTemp);
		if (count > 0) {
			throw new LogicException("请先移除【" + role.getName() + "】下的用户！");
		}
		// 默认生成的不能禁用
		if (StringUtils.isNotBlank(role.getId())) {
			if(null !=role){
				if (Global.IS_DEFAULT.equals(role.getIsDefault())) {
					throw new LogicException("角色'" + role.getName()
							+ "'为基础角色，不能禁用！");
				}
				delete(role);	
			}
		}
	
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
		if(StringUtils.isBlank(ids) || StringUtils.isBlank(roleId) ){
			return ;
		}
		String[] idArray = ids.split(",");
		if(ArrayUtils.isEmpty(idArray)){
			return ;
		}
		User user = null;
		Role role = new Role(roleId);
		for (String userId : idArray) {
			user = new User(userId);
			user.setRole(role);
			userDao.deleteUserRole(user);
		}
	}
	
	
	/**
	 * 移除所有角色用户关联
	 * @param roleId 移除角色Id
	 * add lixc
	 * 2017年10月9日19:49:57
	 */
	@Transactional(readOnly = false)
	@Override
	public void allRemove(String roleId){
		if(StringUtils.isBlank(roleId) ){
			return ;
		}
		 //当前登陆人不能移除自己的用户
		 Role role = dao.get(roleId);
		 User user =userDao.get(UserUtils.getCurrentUserId());
		 user.setRole(role);
		 long count=userDao.findUserRoleCount(user);
		if(count>0){
			throw new LogicException("无法从角色【" + role.getName() + "】中移除用户【" + UserUtils.getCurrentUserName() + "】自己！");
		}else{
			user.setRole(null);
			userDao.deleteUserRole(user);
		}
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
	
	

	/**
	 * 批量修改角色，角色组的关联
	 * @param roleIds
	 * @param roleGroupIds
	 * add 
	 * lixc 
	 * 2017年10月11日19:27:58
	 */
	@Transactional(readOnly = false)
	public void updateRole(String roleIds,String roleGroupIds){
		if(StringUtils.isEmpty(roleIds) || StringUtils.isEmpty(roleGroupIds))
			return ;
		String[] roleIdArray = roleIds.split(",");
		String[] roleGroupArray = roleGroupIds.split(",");
		List<Role> list = new ArrayList<Role>();
		Role role;
		SysRoleGroup sysRoleGroup;
	     if(roleIdArray.length>0 && roleIdArray.length==roleGroupArray.length){
	    	for (int i = 0; i < roleIdArray.length; i++) {
	    		role = new Role();
	    		sysRoleGroup = new SysRoleGroup();
	    		sysRoleGroup.setId(roleGroupArray[i]);
	    		role.setId(roleIdArray[i]);
	    		role.setSysRoleGroup(sysRoleGroup);
	    		list.add(role);
	       }
	    	if(null != list && list.size()>0){
	    	dao.updateBatch(list);
	    	}
	     }
	}
	
}