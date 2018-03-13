package com.taoding.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
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
 
}
