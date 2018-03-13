package com.taoding.service.menu;

import java.util.List;
import java.util.Map;

import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.CrudService;
import com.taoding.domain.menu.Menu;
import com.taoding.domain.user.User;
import com.taoding.mapper.menu.MenuDao;

public interface MenuService extends CrudService<MenuDao, Menu>{

	/**
	 * 基础资源获得菜单列表
	 * @param menu
	 * @return
	 */
	public List<Menu> findByResourceId(Menu menu);
	
	/**
	 * 获得用户访问的列表
	 * @param user
	 * @return
	 */
	public Map<String, Object> getMapByUser(User user);
	
	 /**
     * 基础资源管理页面
     * @param resourceId 角色Id 或 企业配置ID
     * @param type 1 角色  2 企业
     * @param parentIds  父亲ID串 例如 0,1,
     * add lixc 
     * 2017-10-20 13:54:03
     * @return
     */
	public void makeTreeList(TreeNode parenNode,Menu menu,String type,String resourceId);
	
	/**
	 * 资源ID
	 * @author lixc
	 * @date 2017-11-11 17:51:49
	 */
	public Map<String, Object> getResourceByRoleId(String resourceId);
	
	/**
	 * 查询用户菜单权限
	 * @param Menu
	 * @author lixc
	 * @date 2017-11-14 13:51:00
	 */
	public List<Menu> findByUserId(String UserId, String enterpriseBusinessId);
	
	/**
	 * 根据userId查找该用户所能使用的所有权限
	 * @param userId
	 * @param enterpriseId
	 * @return
	 */
	public List<Menu> listMenuByUserId(String userId);
	
	
	
	/**
	 * 获取所有基础资源列表
	 * @return
	 */
	public Menu getALLMenu();
	
	
	/**
	 * 根据基础资源id获取详情
	 * @param menuId
	 * @return
	 */
	public Menu getMenuDetail(String menuId);
	
	
	/**
	 * 根据基础资源id删除基础资源
	 * @param menuId
	 * @return
	 */
	public boolean delMenu(String menuId);
	
	/**
	 * 添加/修改基础资源
	 * @author fc 
	 * @version 2017年12月15日 上午9:42:41 
	 * @param menu
	 * @return
	 */
	public boolean saveMenu(Menu menu);
	
}
