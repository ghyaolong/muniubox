
package com.taoding.controller.assisting;

import java.util.List;

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
import com.taoding.domain.assisting.CpaAssistingBalanceDepartment;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.service.accountingBook.AccountingBookService;
import com.taoding.service.assisting.CpaAssistingBalanceDepartmentService;
import com.taoding.service.assisting.CpaAssistingExpenseTypeService;

/**
 * 辅助核算部门Controller
 * @author csl
 * @version 2017-11-20
 */
@RestController
@RequestMapping(value = "/assisting/department")
public class CpaAssistingBalanceDepartmentController extends BaseController {

	@Autowired
	private CpaAssistingBalanceDepartmentService cpaAssistingBalanceDepartmentService;
	
	@Autowired
	private CpaAssistingExpenseTypeService cpaAssistingExpenseTypeService;
	
	@Autowired
	private AccountingBookService accountingBookService;
	
	/**
	 * 根据id编辑部门的信息
	 * @param department
	 * @return
	 * add csl
	 * 2017-11-23 13:40:19
	 */
	@PutMapping(value="/updateDepart")
	public Object updateDepart(@RequestBody CpaAssistingBalanceDepartment department ){
		if (StringUtils.isEmpty(department.getId())) {
			throw new LogicException("id不能为空！");
		}
		List<CpaAssistingExpenseType> expenseTypes = cpaAssistingExpenseTypeService.findAllList();
		for (CpaAssistingExpenseType depart : expenseTypes) {
			if (depart.getId().equals(department.getExpenseTypeId())) {
				cpaAssistingBalanceDepartmentService.updateDepart(department);
				return true;
			}
		}
		throw new LogicException("费用类型不存在！");	
	}

	/**
	 * 根据id删除部门
	 * @param id
	 * @return
	 * add csl 
	 * 2017-11-22 14:59:56
	 */
	@DeleteMapping("/deleteDepart/{id}")
	public Object deleteDepart(@PathVariable("id") String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("辅助核算类型中的部门id为空！");
		}
		cpaAssistingBalanceDepartmentService.deleteDepart(id);
		return true;
	}
	
	/**
	 * 查询所有的部门
	 * @return
	 * add csl 
	 * 2017-11-23 14:33:16
	 */
	@PostMapping("/findDepart")
	public Object findDepart(@RequestBody CpaAssistingBalanceDepartment  department){
		 return cpaAssistingBalanceDepartmentService.findList(department);
	}
}