package com.taoding.controller.customerTaxItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.customerTaxItem.TaxValueAdded;
import com.taoding.service.customerTaxItem.TaxValueAddedService;
/**
 * 客户增值税设置controller
 * @author mhb
 * @version 2017-11-27
 */
@RestController
@RequestMapping(value = "/customerTaxAdded")
public class TaxValueAddedController extends BaseController{
	@Autowired
	private TaxValueAddedService taxValueAddedService;
	/**
	 * 客户增值税添加
	 * @param taxValueAdded  实体类 
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	@PostMapping("/save")
	public Object save(@RequestBody TaxValueAdded taxValueAdded) {
		return taxValueAddedService.insert(taxValueAdded);
	}
	/**
	 * 查询增值税
	 * @param accountingId  账薄id 
	 * @return
	 * @author mhb
	 * @Date 2017年11月27日
	 */
	@GetMapping("/get/{accountingId}")
	public Object findIncrementTax(@PathVariable("accountingId") String accountingId) {
		return taxValueAddedService.getIncrementTax(accountingId);
	}
}
