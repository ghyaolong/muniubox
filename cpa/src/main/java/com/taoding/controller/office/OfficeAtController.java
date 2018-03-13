package com.taoding.controller.office;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.serviceUtils.EnterpriseUtils;
import com.taoding.common.utils.StringUtils;
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
	@GetMapping("/treeData")
	public Object treeData() {
		return officeService.treeOffice();
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
		if(office==null){
			return false;
		}
		officeService.nextLowerSave(office);
		return "添加成功!";
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
		officeService.deleteOffice(officeId);
		return "删除成功!";
	}

   /**
	 * 组织机构禁用
	 * 
	 * @param office
	 * @return add mhb 
	 * @Date 2017年11月08日11:54:56
	 */
	@PutMapping("/deleteAt/{id}")
	public Object deleteAt(@PathVariable("id") String id) {
		if(StringUtils.isNotBlank(id)){
			officeService.deleteAtOffice(new Office(id));
		}
		return "禁用成功!";
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
	public Object listData(HttpServletRequest request) {
		Map<String,Object> queryMap = getRequestParams(request);
		return  officeService.findOfficeForUser(queryMap);
	}
}
