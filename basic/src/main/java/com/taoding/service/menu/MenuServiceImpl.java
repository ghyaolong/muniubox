package com.taoding.service.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.EnterpriseUtils;
import com.taoding.common.utils.UserUtils;
import com.taoding.domain.menu.Menu;
import com.taoding.domain.user.User;
import com.taoding.mapper.menu.MenuDao;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl extends DefaultCurdServiceImpl<MenuDao, Menu> 
	implements MenuService{

	@Override
	public List<Menu> findByResourceId(Menu menu) {
		return dao.findByResourceId(menu);
	}

	
	@Override
	public Map<String, Object> getMapByUser(User user) {
		if(null == user){
			return null;
		}
		
		//用户所属企业配置业务
		Map<String, String> userBusinessMap = Maps.newHashMap();
		
		userBusinessMap.put("platform", EnterpriseUtils.getBaseMenuId());
		userBusinessMap.put("cpa", "cpa_id12345678910");
		
		//根据用户配置的业务加载菜单权限
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(MapUtils.isNotEmpty(userBusinessMap)){
			Set<Entry<String, String>> entryMap=userBusinessMap.entrySet();
			if(null != entryMap){
				for (Entry<String, String> entry : entryMap) {
					map.put(entry.getKey(),getPermissionByPlatform(user.getId(),entry.getValue()));
				}
			}
		}
		map.put("loginName", user.getLoginName());
		map.put("userName", user.getName());

		return map;
	}
	
	private List<String> getPermissionByPlatform(String userId ,String platformId) {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(platformId)){
			return null;
		}
		
	  List<Menu> list=dao.findByUserId(userId,platformId,DataEntity.DEL_FLAG_NORMAL);
		List<String> permissions =new ArrayList<String>();//Collections3.extractToList(list, "permission");
		if(!CollectionUtils.isEmpty(list)){
			for (Menu m : list) {
				if(StringUtils.isNotBlank(m.getPermission())){
					if(StringUtils.isNotBlank(m.getPermission())){
						permissions.add(m.getPermission()); 
					}
				}
			}
		}
		return permissions;
	}
	
	
	/**
	 * 构建资源树
	 * @param parenNode
	 * @param type
	 * @param resourceId
	 * @author lixc
	 * @date 2017年11月11日15:44:15
	 */
	public void makeTreeList(TreeNode parenNode,Menu menu,String type,String resourceId){
		if(null == parenNode ||menu == null){
			return ;
		}
         menu.getSqlMap().put("resourceId", resourceId);
         menu.getSqlMap().put("type", type);
         menu.setParentIds(menu.getParentIds()+menu.getId()+",");
         
		 List<Menu> cMenuList= dao.findByResourceId(menu);
		 if(!CollectionUtils.isEmpty(cMenuList)){
			 for (Menu m : cMenuList) {
				 TreeNode temp= new TreeNode(m.getId(), m.getName());
				 parenNode.getChildren().add(temp);
				
				 makeTreeList(temp,m,type,resourceId);
			}
		 }
	}
	
	public Map<String, Object> getResourceByRoleId(String resourceId){
		
		TreeNode parenNode = new TreeNode();
		parenNode.setId("0");
		parenNode.setLabel("菜单管理");
		
		Menu menu = new Menu();
		menu.getSqlMap().put("type", "1");
		menu.getSqlMap().put("resourceId", resourceId);
		List<Menu> checkList = dao.findByResourceId(menu);
		
		menu.setParentIds("0,");
		menu.setId("1");
		makeTreeList(parenNode,menu, "2", EnterpriseUtils.getBaseMenuId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menus", parenNode);
		map.put("checkList", Collections3.extractToList(checkList, "id"));
		return map;
	}
	
	public List<Menu> findByUserId(String userId, String enterpriseBusinessId){
		return dao.findByUserId(userId, enterpriseBusinessId, DataEntity.DEL_FLAG_NORMAL);
	}
	
	
	@Override
	public List<Menu> listMenuByUserId(String userId){
		List<Menu> menuList = dao.listMenuByUserId(userId);
		List<Menu> notEmptyMenuList = new ArrayList<Menu>();
		for (Menu nemu : menuList) {
			if (null != nemu && StringUtils.isNotEmpty(nemu.getHref())) {
				notEmptyMenuList.add(nemu);
			}
		}
		return notEmptyMenuList;
	}


	@Override
	public Menu getALLMenu() {
		List<Menu> allMenuList = dao.getALLMenu();
		Menu menu = new Menu();
		for(Menu m : allMenuList){
			if(m.getId().equals("1")){
				menu = m;
				break;
			}
		}
		if(menu != null){
			makeMenuTree(allMenuList,menu);
		}
		return menu;
	}
	
	/**
	 * 递归添加子资源
	 * @param menuId
	 * @param menuList
	 * @param menu
	 */
	public void makeMenuTree(List<Menu> menuList,Menu menu){
		List<Menu> mList = new ArrayList<Menu>();
		for(Menu m : menuList){
			if(m.getParentId().equals(menu.getId())){
				mList.add(m);
			}
		}
		if(mList.size() <= 0){
			return;
		}else{
			menu.setSubMenu(mList);
			for(int i=0; i<menu.getSubMenu().size(); i++){
				makeMenuTree(menuList,menu.getSubMenu().get(i));
			}
		}
	}


	@Override
	public Menu getMenuDetail(String menuId) {
		Menu menu = dao.getMenuDetail(menuId);
		if(menu != null && menu.getParent() != null && !menu.getParentId().equals("")){
			menu.setParent(dao.getMenuDetail(menu.getParentId()));
		}
		return menu;
	}


	@Override
	public boolean delMenu(String menuId) {
		int i = dao.delMenu(menuId,Menu.DEL_FLAG_DELETE);
		if(i > 0){
			return true;
		}
		return false;
	}


	@Override
	public boolean saveMenu(Menu menu) {
		Menu menuParent = null;
		if(menu != null && menu.getParent() != null && !"".equals(menu.getParentId())){
			menuParent = dao.getMenuDetail(menu.getParentId());
		}
		if(menuParent != null){
			menu.setParentIds(menuParent.getParentIds() +  menuParent.getId() + ",");
		}else{
			return false;
		}
		menu.setDelFlag(Menu.DEL_FLAG_NORMAL);
		this.save(menu);
		return true;
	}

}
