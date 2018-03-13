 
package com.taoding.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.role.Role;
import com.taoding.domain.role.SysRoleGroup;
import com.taoding.service.role.RoleService;
import com.taoding.service.role.SysRoleGroupService;

@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
	
	@Autowired
	private SysRoleGroupService sysRoleGroupService;
	
	
	@Autowired 
	private RoleService roleService;
	/**
	 * 角色组、角色树形结构
	 * @param queryCondition 查询关键字
	 * @return
	 * lixc
	 */
	@GetMapping("/sysGroupTree")
	public Object getSysGroupTree(@RequestParam(value="queryCondition",required=false) String queryCondition){
	    //预留企业标示，用于查看不同企业的数据
		 SysRoleGroup sysRoleGroup = new SysRoleGroup();
		 
		 if(StringUtils.isNotEmpty(queryCondition) && StringUtils.isNotBlank(queryCondition)){
			 sysRoleGroup.getSqlMap().put("queryConditionSqlG", QueryConditionSql.getQueryConditionSql("s",QueryConditionFieldsArray.groupQueryFields, queryCondition));
			 sysRoleGroup.getSqlMap().put("queryConditionSqlR", QueryConditionSql.getQueryConditionSql("r",QueryConditionFieldsArray.roleQueryFields, queryCondition));
		 }
		 
		return  sysRoleGroupService.findSysGroupTree(sysRoleGroup);
	}

	/**
	 * 获得角色组
	 * @param id
	 * @return 角色组实例
	 * @date 2017年11月10日17:45:52
	 * @author lixc 
	 */
	@GetMapping("/getRoleGroup/{id}")
	public Object getRoleGroup(@PathVariable("id") String id){
		SysRoleGroup entity  = sysRoleGroupService.get(id);
		return entity;
	}
	
	
	/**
	 * 保存角色组
	 * @param sysRoleGroup
	 * @return 
	 * @date 2017-11-10 18:16:57
	 * @author lixc
	 */
	@PostMapping("/saveRoleGroup")
	public Object saveRoleGroup(@RequestBody SysRoleGroup sysRoleGroup) {
			//重新获取角色组表号
			if(StringUtils.isEmpty(sysRoleGroup.getId())){//添加时验证角色组编号
				String getGroupNo = sysRoleGroupService.getGroupNo();//获得角色组编号
				sysRoleGroup.setRoleGroupNo(getGroupNo);
			}
			sysRoleGroupService.save(sysRoleGroup);
		return true;
	}
	 
    /**
     * 角色组删除
     * @param id
     * @return
     * @author lixc
     * @date 2017年11月11日10:11:34
     */
	@DeleteMapping(value = "/delete/{id}")
	public Object delete(@PathVariable("id") String id) throws LogicException {
			
		if(!sysRoleGroupService.checkRoleGroup(id, "1")){
				SysRoleGroup sysRoleGroup = sysRoleGroupService.get(id);
				sysRoleGroupService.delete(sysRoleGroup);
			} else{
				throw new LogicException("请移除该角色组下的角色！");
			}
		 
		return true;
	}
	

	/**
	 * 获得角色组编号
	 * @return
	 * @author lixc
	 * @date  2017-11-11 11:37:34
	 */
	@GetMapping(value = "/getGroupNo")
	public Object getGroupNo() {
		return sysRoleGroupService.getGroupNo();
	}
	
	/**
	 * 获得角色编号
	 * @return
	 * @author lixc
	 * @date  2017-11-11 11:37:34
	 */
	@GetMapping(value = "/findRoleNo")
	public Object findRoleNo() {
		return roleService.findRoleNo();
	}
	
	
	/**
	 * 保存角色
	 * @param role
	 * @return
	 * @author lixc
	 * @date 2017-11-11 11:56:28
	 */
	@PostMapping(value = "/saveRole")
	public Object saveRole(@RequestBody Role role) {
		roleService.saveRole(role);
		return true;
	}
	
	/**
	 * 删除角色
	 * @return
	 * @author lixc
	 * @2017年11月11日15:04:02
	 */
	@DeleteMapping(value="/deleteRole/{id}")
    public Object deleteRole(@PathVariable("id") String id){
    	Role role = new Role();
    	role.setId(id);
		roleService.delete(role);
    	return true;
    }
	
	
	/**
	 * 角色授权提交
	 * @param roelId
	 * @param menuIds
	 * @return
	 * @author lixc
	 * @date 2017-11-11 18:00:12
	 */
	@PostMapping(value = "/saveRoleMenu")
	public Object saveRoleMenu(@RequestParam(value="roleId",required=false)String roleId,
			                   @RequestParam(value="menuIds",required=false)String menuIds) {
		   if(StringUtils.isNotBlank(menuIds)){
			   roleService.saveRoleMenu(menuIds, roleId);
		   }
		return true;
	}
	
	/**
	 *批量移除
	 * @param ids
	 * @param roleId
	 * @return
	 * @author lixc
	 * @date 2017年11月11日19:11:05
	 */
	@DeleteMapping(value = "/deleteBatchRemove")
	public Object deleteBatchRemove(@RequestParam(value="ids",required=true) String ids, @RequestParam(value="roleId",required=true) String roleId) {
		if(StringUtils.isNotEmpty(ids)&&StringUtils.isNotEmpty(roleId)){
			//当前登陆人不能移除自己的用户
			/*Role role = roleService.get(roleId);
			if(UserUtils.getUser().getId().indexOf(ids)>0){
				//XXX 待完成
				//throw new LogicException("无法从角色【" + role.getName() + "】中移除用户【" + UserUtils.getUser().getName() + "】自己！");
				throw new LogicException("无法从角色【" + role.getName() + "】中移除用户【】自己！");
			}else{
				roleService.deleteBatchRemove(ids,roleId);
			}*/
			roleService.deleteBatchRemove(ids,roleId);
		}else{
			throw new LogicException("参数不能为空！");
		}
		return true;
	}
	
	/**
	 * 角色用户关联
	 * @param ids
	 * @param roleId
	 * @return
	 * @author lixc
	 * @date 2017年11月11日19:44:14
	 */
	@PostMapping("/insertBatch")
	public Object insertBatch(@RequestParam(value="ids",required=true) String ids, 
			                     @RequestParam(value="roleId",required=true) String roleId) {
		roleService.insertBatchJson(ids, roleId);
		return true;
	}
}
