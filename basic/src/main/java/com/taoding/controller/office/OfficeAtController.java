package com.taoding.controller.office;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.office.Office;
import com.taoding.service.office.OfficeService;

@RestController
@RequestMapping(value = "/officeAt")
public class OfficeAtController extends BaseController {

	@Autowired
	private OfficeService officeService;

	/**
	 * 获取机构树形机构数据
	 * 
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	@PostMapping("/treeData")
	public Object treeData(@RequestBody Map<String,Object> queryMap) {
		return officeService.treeOffice(queryMap);
	}

	/**
	 * 组织机构添加
	 * 
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	@PostMapping("/save")
	public Object save(@RequestBody Office office) {
		return officeService.nextLowerSave(office);
	}
	
	/**
	 * 组织机构编辑
	 * 
	 * @param office
	 * @return 
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	@PutMapping("/edit")
	public Object edit(@RequestBody Office office) {
		return officeService.edit(office);
	}

	/**
	 * 组织机构删除
	 * 
	 * @param office
	 * @return
	 * @author mhb 
	 * @date 2017年11月08日11:54:56
	 */
	@PutMapping("/delete/{id}")
	public Object delete(@PathVariable("id") String officeId) {
		return officeService.deleteOffice(officeId);
	}

   /**
	 * 组织机构禁用
	 * 
	 * @param office
	 * @return add mhb 
	 * @Date 2017年11月08日11:54:56
	 */
	@PutMapping("/deleteAt/{id}/{useable}")
	public Object deleteAt(@PathVariable("id") String id,@PathVariable("useable") String useable) {
		return officeService.deleteAtOffice(id,useable);
	}

	/**
	 * 详细,编辑页面数据查询
	 * 
	 * @param office
	 * @return add mhb 
	 * @Date 2017年11月09日11:54:56
	 */
	@GetMapping("/formAt/{id}")
	public Object formAt(@PathVariable("id") String id) {
		 return officeService.getEditOffice(id);
	}

   /**
	 * 查询部门下的员工
	 * 
	 * @param office
	 * @return 
	 * @Date 2017年11月08日11:54:56
	 */
	@PostMapping("/employeeList")
	public Object listData(@RequestBody Map<String,Object> queryMap) {
		return  officeService.findOfficeForUser(queryMap);
	}
	
	/**
	 * 
	* @Description: TODO(获得组织机构，用户树形) 
	* @return Object 返回类型    
	* @throws 
	* @author lixc
	* @date 2017年11月23日
	 */
	@GetMapping("/treeOfficeAndUser")
	public Object treeOfficeAndUser(){
	return 	officeService.treeOfficeAndUser();
	}
}
