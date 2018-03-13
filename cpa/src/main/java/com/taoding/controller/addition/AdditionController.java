package com.taoding.controller.addition;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.addition.CpaBank;
import com.taoding.domain.addition.CpaCustomerSubjectBank;
import com.taoding.service.addition.CpaBankService;
import com.taoding.service.addition.CpaCustomerSubjectBankService;

/**
 * 客户管理其他信息接口添加Controller
 * 
 * @author mhb
 * @version 2017-11-16
 */
@RestController
@RequestMapping(value = "/customer/addition")
public class AdditionController {

	@Autowired
	private CpaBankService cpaBankService;
	@Autowired
	private CpaCustomerSubjectBankService cpaCustomerSubjectBankService;
    
	/**
	 * 查询银行所有信息
	 * @param cpaBank 银行Entity
	 * @return
	 */
	@PostMapping("/bank/getAll")
	public Object getAllBankList(@RequestParam(value = "bankName", required = false) String bankName) {
		return cpaBankService.getBankList(bankName);
	}
	/**
	 * 增加银行信息
	 * 
	 * @param cpaBank
	 *            银行Entity
	 * 
	 * @return
	 */
	@PostMapping("/bank/save")
	public Object saveBank(@RequestBody CpaBank cpaBank) {
		return cpaBankService.saveBank(cpaBank);
	}

	/**
	 * 修改银行信息
	 * 
	 * @param cpaBank
	 *            银行Entity
	 * 
	 * @return
	 */
	@PostMapping("/bank/edit")
	public Object editBank(@RequestBody CpaBank cpaBank) {
		return cpaBankService.editBank(cpaBank);
	}

	/**
	 * 删除银行信息
	 * 
	 * @param id
	 * 
	 * @return
	 */
	@PutMapping("/bank/delete/{id}")
	public Object deleteBank(@PathVariable("id") String id) {
		return cpaBankService.deleteBank(id);
	}
	
	/**
	 * 添加客户科目银行信息
	 * 
	 * @param  customerSubjectBank
	 * @return
	 */
	@PostMapping("/customerSubjectBank/save")
	public Object saveCustomerSubjectBank(@RequestBody CpaCustomerSubjectBank customerSubjectBank) {
		return cpaCustomerSubjectBankService.saveCustomerSubjectBank(customerSubjectBank);
	}
	/**
	 * 修改客户科目银行信息
	 * 
	 * @param  customerSubjectBank
	 * @return
	 */
	@PostMapping("/customerSubjectBank/edit")
	public Object editCustomerSubjectBank(@RequestBody CpaCustomerSubjectBank customerSubjectBank) {
		return cpaCustomerSubjectBankService.editCustomerSubjectBank(customerSubjectBank);
	}
	/**
	 * 删除客户科目银行信息
	 * 
	 * @param  customerSubjectBank
	 * @return
	 */
	@PostMapping("/customerSubjectBank/delete/{id}")
	public Object deleteCustomerSubjectBank(@PathVariable("id") String id) {
		return cpaCustomerSubjectBankService.deleteCustomerSubjectBank(id);
	}
	
	/**
	 * 查询客户下的客户银行科目列表
	 * @param  customerId 客户id
	 * @TODO 其他参数待定
	 * @return
	 */
	@PostMapping("/customerSubjectBank/listData")
	public Object customerSubjectBankFindList(@RequestBody  Map<String,Object> queryMap ) {
		if(StringUtils.isBlank(String.valueOf(queryMap.get("customerId")))){
			throw new LogicException("客户信息为空");
		} 
		return cpaCustomerSubjectBankService.selectList(queryMap);
	}
	
	

}
