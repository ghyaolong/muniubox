package com.taoding.service.role;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taoding.common.entity.DataEntity;
import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.domain.user.User;
import com.taoding.mapper.role.RoleDao;
import com.taoding.mapper.role.SysRoleGroupDao;


/**
 * 角色组管理Service
 * @author lixc
 * @version 2017-09-29
 */
@Service
public class SysRoleGroupServiceImpl extends DefaultCurdServiceImpl<SysRoleGroupDao, SysRoleGroup> implements SysRoleGroupService {
	
	@Autowired
	private  RoleDao roleDao;
	/**
	 * 按条件获得角色树
	 * @param queryMap
	 * @return List<Map<String, String>>
	 * @date  2017年11月10日16:19:40
	 * @author lixc 
	 */
	@Override
	public TreeNode findSysGroupTree(SysRoleGroup sysRoleGroup) {
		
		if(null==sysRoleGroup){
			return null;
		}
		TreeNode node =null;
		List<SysRoleGroup> groupList= dao.findList(sysRoleGroup);
		if(!CollectionUtils.isEmpty(groupList)){
			
			node= new TreeNode("0", "角色组");
			TreeNode nodeTemp=null;
			Role role = new Role();
			if(null != sysRoleGroup.getSqlMap()){
				role.setSqlMap(sysRoleGroup.getSqlMap());
			}
			List<Role> roleList=null;
			role.setEnterpriseMarking(sysRoleGroup.getEnterpriseMarking());
			for (SysRoleGroup sg: groupList) {
				nodeTemp= new TreeNode(sg.getId(), sg.getRoleGroupName());
				nodeTemp.setAttr(SysRoleGroup.GROUP.replace("_", ""));//区分角色组，角色
				node.getChildren().add(nodeTemp);
				role.setSysRoleGroup(sg);
				roleList=roleDao.findAllList(role);
				if(!CollectionUtils.isEmpty(roleList)){
					for (Role r : roleList) {
					nodeTemp.setAttr(Role.ROLE.replace("_", ""));
					nodeTemp.getChildren().add(new TreeNode(r.getId(), r.getName()));
					}
				}
			}
			
		}else{
			return null;
		}
		
		return  node;
	}
	
	@Override
	public String  getGroupNo(){
		String mark=EnterpriseUtils.getCurrentEnterMark();
		SysRoleGroup sysRoleGroup = new SysRoleGroup();
		sysRoleGroup.setRoleGroupNo(mark);
		sysRoleGroup.setEnterpriseMarking(EnterpriseUtils.getCurrentUserEnter());
		String markStr=dao.findMaxGroupNo(sysRoleGroup);
		if(StringUtils.isBlank(markStr)||StringUtils.isEmpty(markStr)){
			markStr =mark+"0000001";
		}else{
			markStr=markStr.replace(mark, "");
			int numMark=0;
			try {
				  numMark=Integer.parseInt(markStr);
			} catch (Exception e) {
				 
			}
			numMark=numMark+1;
 			markStr=mark+String.format("%07d", numMark);
		}
		
		return markStr;
	}
	
	
	public long findRoleCount(Role role){
		return roleDao.findRoleCount(role);
	}
	
	
	 /**
	  * 检查角色组下用角色是否存在
	  * @param id
	  * @param operation 1 删除  2禁用
	  * @author lixc
	  * @date 2017-11-11 10:07:24
	  */
	public boolean checkRoleGroup(String id,String operationType){
		if(StringUtils.isNotEmpty(operationType) && StringUtils.isNotEmpty(id)){
		if("1".equals(operationType)){
			Role role = new Role();
			SysRoleGroup sysRoleGroup = new SysRoleGroup();
			sysRoleGroup.setId(id);
			role.setSysRoleGroup(sysRoleGroup);
		  long roleCount=findRoleCount(role);
		  if(roleCount>0){
			  return true;
		  }
		}
		}
		return false;
	}

	/**
	 * 插入模板角色组数据
	 * @param roleGroupList
	 * @param user
	 * @param enterpriseTemplate
	 * @param strId
	 * @return
	 * mhb
	 * 2017.10.30 20:39
	 */
	public void saveEnterpriseTemplateSysRoleGroupList(List<SysRoleGroup> roleGroupList, User user,String enterpriseTemplate,String strId) {
		try {

			SysRoleGroup sysRoleGroupEnterpriseTemplateGroup = new SysRoleGroup();
			sysRoleGroupEnterpriseTemplateGroup.setEnterpriseMarking(enterpriseTemplate);
			sysRoleGroupEnterpriseTemplateGroup.setDelFlag(DataEntity.DEL_FLAG_NORMAL);
			/*查询角色组模板 */
			List<SysRoleGroup> enterpriseTemplateRoleGroupList=  dao.findEnterpriseTemplateForRoleGroup(sysRoleGroupEnterpriseTemplateGroup);
			for (SysRoleGroup sysRoleGroup : enterpriseTemplateRoleGroupList) {
				  sysRoleGroup.setId(sysRoleGroup.getId()+strId);
				  /*sysRoleGroup.setRoleGroupNo(sysRoleGroup.getRoleGroupNo()+strId);*/
				  sysRoleGroup.setRoleGroupStatus(Global.START_USERING);
				  sysRoleGroup.setEnterpriseMarking(strId);
				  sysRoleGroup.setUser(user);
				  roleGroupList.add(sysRoleGroup);
				}
			dao.insertEnterpriseTemplateForRoleGroup(roleGroupList);	
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
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
			String enterpriseTemplate,String strId) {
		try {
				Role roleEnterpriseTemplate= new Role();
				roleEnterpriseTemplate.setEnterpriseMarking(enterpriseTemplate);
				roleEnterpriseTemplate.setDelFlag("0");
			List<Role>  roles	 =roleDao.findEnterpriseTemplateForRole(roleEnterpriseTemplate);
				for (Role role : roles) {
					role.setId(role.getId()+strId);
					role.setEnname(role.getEnname());
					role.setRoleGroupId(role.getRoleGroupId()+strId);
					role.setEnterpriseMarking(strId);
					role.setUser(user);
					roleList.add(role);
					}
				roleDao.insertEnterpriseTemplateSysRoleList(roleList);
		} catch (Exception e) {
						e.printStackTrace(); 
					}
		}

	public void saveEnterpriseTemplateSysUserRoleList(List<Role> roleList) {
		 roleDao.insertEnterpriseTemplateSysUserRoleList(roleList);
		}
	
	/**
	 * 插入模板用户角色组数据
	 * @param roleGroupList
	 * @return
	 * mhb
	 * 2017.10.30 20:39
	 */
	public void saveEnterpriseTemplateSysUserRoleGroupList( List<SysRoleGroup> roleGroupList) {
		dao.insertEnterpriseTemplateSysUserRoleGroupList(roleGroupList);
	}
	 
	
}