package com.taoding.controller.customerTaxItem;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.domain.customerTaxItem.CustomerTaxItem;
import com.taoding.service.customerTaxItem.CustomerTaxItemService;

/**
 * 客户税项设置controller
 * @author mhb
 * @version 2017-11-22
 */
@RestController
@RequestMapping(value = "/customerTax")
public class CustomerTaxItemController {
	@Autowired
	private CustomerTaxItemService customerTaxItemService;
	/**
	 * 新增客户税项
	 * @param customerTaxItem  实体类 
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PostMapping("/save")
	public Object saveTax(@RequestBody CustomerTaxItem customerTaxItem) {
		return customerTaxItemService.saveTax(customerTaxItem);
	}
	/**
	 * 查询税项
	 * @param id  税项 
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@GetMapping("/get/{id}")
	public Object formTax(@PathVariable("id")String id) {
		return customerTaxItemService.selectTaxById(id);
	}
	/**
	 * 修改客户税项
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/edit")
	public Object editTax(@RequestBody CustomerTaxItem customerTaxItem) {
		return customerTaxItemService.editTax(customerTaxItem);
	}
	/**
	 * 删除客户税项
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/delete/{id}")
	public Object deleteTax(@PathVariable("id")String id) {
		return customerTaxItemService.deleteTax(id);
	}
	/**
	 * 启用 禁用客户税项
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/enable/{id}")
	public Object enableTax(@PathVariable("id")String id) {
		return customerTaxItemService.enableTax(id);
	}
	/**
	 * 查询税项
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PostMapping("/listData")
	public Object findCustomerTaxTemplate(@RequestBody Map<String, Object> queryMap) {
		return customerTaxItemService.findCustomerTaxList(queryMap);
	}


}
