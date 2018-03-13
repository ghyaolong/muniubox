package com.taoding.controller.area;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taoding.common.controller.BaseController;
import com.taoding.service.area.AreaService;

@RestController
@RequestMapping(value = "/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	/**
	 * 获取省市树结构数据
	 * 
	 * @return
	 */
	@GetMapping("/treeData")
	public Object getTreeArea() {
		return areaService.getTreeArea();
	}

}
