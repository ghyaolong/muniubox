package com.taoding.controller.menu;

import java.util.List;

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
import com.taoding.domain.menu.Menu;
import com.taoding.service.menu.MenuService;

/**
 * 菜单管理
 * @author lixc
 * @date 2017年11月11日15:19:32
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	  /**
     * 基础资源管理页面
     * @param resourceId 角色Id 或 企业配置ID
     * @param type 1 角色  2 企业
     * add lixc 
     * 2017-10-20 13:54:03
     * @return
     */
	@GetMapping("/basicResourceTree")
	public Object basicResourceTree(@RequestParam(value="resourceId",required=true)String resourceId){
		return menuService.getResourceByRoleId(resourceId);
	}
	
	@GetMapping("/userMenu/{id}")
	public Object getMenuByUserId(@PathVariable("id") String userId) {
		List<Menu> menuList = menuService.listMenuByUserId(userId);
		return menuList;
	}
	
	/**
	 * 所有基础资源列表
	 * add fc 
     * 2017-12-14 10:20:03
	 * @return
	 */
	@GetMapping("/allMenuList")
	public Object getAllMenu() {
		Menu allMenu = menuService.getALLMenu();
		return allMenu;
	}
	
	/**
	 * 根据基础资源id获取详情
	 * @param menuId
	 * @return
	 */
	@GetMapping("/menuDetail/{id}")
	public Object getMenuDetail(@PathVariable("id") String menuId) {
		Menu menu = menuService.getMenuDetail(menuId);
		return menu;
	}
 
	/**
	 * 根据基础资源id删除基础资源
	 * @param menuId
	 * @return
	 */
	@DeleteMapping("/delMenu/{id}")
	public Object delMenu(@PathVariable("id") String menuId){
		boolean isDel = menuService.delMenu(menuId);
		return isDel;
	}
	
	
	/**
	 * 添加/修改基础资源
	 * @author fc 
	 * @version 2017年12月15日 上午9:11:28 
	 * @return
	 */
	@PostMapping("/saveMenu")
	public Object saveMenu(@RequestBody Menu menu){
		boolean isSave = menuService.saveMenu(menu);
		return isSave;
	}
}
