package com.taoding.controller.customerTaxItem;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.customerTaxItem.CustomerTaxFormula;
import com.taoding.service.customerTaxItem.CustomerTaxFormulaService;

/**
 * 客户税项公式controller
 * @author mhb
 * @version 2017-11-24
 */
@RestController
@RequestMapping(value = "/customerTaxFormula")
public class CustomerTaxFormulaController extends BaseController{
	
	@Autowired
	private CustomerTaxFormulaService customerTaxFormulaService;
	
	/**
	 * 新增客户税项公式
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PostMapping("/save")
	public Object saveTaxFormula(@RequestBody CustomerTaxFormula CustomerTaxFormula) {
		return customerTaxFormulaService.saveTaxFormula(CustomerTaxFormula);
	}
	
	
	/**
	 * 查询客户税项公式
	 * @param queryMap
	 * @return
	 * @author mhb
	 * @Date 2017年11月28日
	 */
	@PostMapping("/listData")
	public Object findList(@RequestBody Map<String,Object> queryMap) {
		return customerTaxFormulaService.findList(queryMap);
	}
	 
}
