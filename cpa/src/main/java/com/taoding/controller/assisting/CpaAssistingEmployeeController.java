
package com.taoding.controller.assisting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingEmployee;
import com.taoding.service.assisting.CpaAssistingEmployeeService;

/**
 * 辅助核算模块员工Controller
 * @author csl
 * @version 2017-11-20
 */
@RestController
@RequestMapping(value = "/assisting/employee")
public class CpaAssistingEmployeeController extends BaseController {
	
	@Autowired
	private CpaAssistingEmployeeService cpaAssistingEmployeeService;
	
	
	/**
	 * 根据id编辑员工的信息
	 * @param department
	 * @return
	 */
	@PutMapping(value="/updateEmployee")
	public Object updateEmployee(@RequestBody CpaAssistingEmployee employee){
		cpaAssistingEmployeeService.updateEmployee(employee);
		return true;
	}

	/**
	 * 根据id删除员工
	 * @param id
	 * @return
	 * add csl 
	 * 2017-11-22 14:59:56
	 */
	@DeleteMapping("/deleteEmployee/{id}")
	public Object deleteEmployee(@PathVariable("id") String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("辅助核算类型中的员工id为空！");
		}
		cpaAssistingEmployeeService.deleteEmployee(id);
		return true;
	}
	
	/**
	 * 根据账薄id查找企业所有的员工
	 * @param cpaAssistingEmployee
	 * @return
	 */
	@PostMapping("/findEmployee")
	public Object findList(@RequestBody CpaAssistingEmployee cpaAssistingEmployee){
		return cpaAssistingEmployeeService.findList(cpaAssistingEmployee);
	}

}