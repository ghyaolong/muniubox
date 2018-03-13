package com.taoding.service.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taoding.common.entity.BaseEntity;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.entity.TreeNode;
import com.taoding.common.service.DefaultCurdServiceImpl;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.Collections3;
import com.taoding.domain.menu.Menu;
import com.taoding.domain.user.User;
import com.taoding.mapper.menu.MenuDao;
import com.taoding.mapper.role.RoleDao;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl extends DefaultCurdServiceImpl<MenuDao, Menu> 
	implements MenuService{

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<Menu> findByResourceId(Menu menu) {
		return dao.findByResourceId(menu);
	}

	
	@Override
	public Map<String, Object> getMapByUser(User user) {
		if(null == user){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<Menu> list = dao.findByUserId(user.getId(),EnterpriseUtils.getBaseMenuId(),DataEntity.DEL_FLAG_NORMAL);
		List<Map<String, String>> menus = new ArrayList<Map<String, String>>();//Collections3.extractToList(list, "href");
		List<String> permissions = new ArrayList<String>();//Collections3.extractToList(list, "permission");
		Map<String, String> tempMap = null;
		if(!CollectionUtils.isEmpty(list)){
			for (Menu m : list) {
				if("1".equals(m.getIsShow()) && StringUtils.isNotBlank(m.getHref())){
					tempMap = new HashMap<String, String>();
					tempMap.put("name", m.getName());
					tempMap.put("href",m.getHref());
					tempMap.put("meunId",m.getId());
					menus.add(tempMap);
				}
			      
				if(StringUtils.isNotBlank(m.getPermission())){
					if(StringUtils.isNotBlank(m.getHref())){
						permissions.add(m.getHref()); 
					}
				}
			}
			if(!Collections3.isEmpty(menus)){
				map.put("menus", menus);
			}
			if(!Collections3.isEmpty(permissions)){
				map.put("permissions", permissions);
			}
		}
		return map;
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
		menu.setParentIds("0,");
		menu.setId("1");
		makeTreeList(parenNode,menu, "2", EnterpriseUtils.getBaseMenuId());
		
		List<Menu> checkList= dao.findByResourceId(menu);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meuns", parenNode);
		map.put("checkList", Collections3.extractToList(checkList, "id"));
		return map;
	}
	
	public List<Menu> findByUserId(String userId,String enterpriseBusinessId){
		return dao.findByUserId(userId,enterpriseBusinessId,DataEntity.DEL_FLAG_NORMAL);
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
}
