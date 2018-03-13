package com.taoding.controller.checkoutSetting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.checkoutSetting.SettleAccountCustomer;
import com.taoding.service.checkoutSetting.SettleAccountCustomerService;
import com.taoding.service.checkoutSetting.SettleAccountOperatingSettingService;
@RestController
@RequestMapping(value = "checkout")
public class SettleAccountCustomerController extends BaseController{
	
	@Autowired
	private SettleAccountCustomerService settleAccountCustomerService;
	@Autowired
	private SettleAccountOperatingSettingService settleAccountOperatingSettingService;
	
	/**
	 * 根据账薄id获取信息(常规检测设置,其他异常检测) 
	 * @param id 账薄id
	 * @return
	 */
	@GetMapping("/info/{bookId}")
	public Object findCheckListData(@PathVariable("bookId") String bookId) {
		if(StringUtils.isEmpty(bookId)){
			throw new LogicException("账薄信息为空");
		}
	Map<String, Object> maps= new HashMap<String, Object>();	
	List<SettleAccountCustomer>	generalTypeValidationList=settleAccountCustomerService.findGeneralTypeValidationListData(bookId);
	List<SettleAccountCustomer>	otherTypeValidationList=settleAccountCustomerService.findOtherTypeValidationListData(bookId);
	maps.put("generalTypeValidationList", generalTypeValidationList);
	maps.put("otherTypeValidationList", otherTypeValidationList);
	return maps;
	}
	
	/**
	 * 启用 禁用
	 * @param id 
	 * @return
	 */
	@PutMapping("/enabled/{id}")
	public Object updateEnabled(@PathVariable("id") String id) {
		return  settleAccountCustomerService.updateEnabled(id);
	}
	
	/**
	 * 查询经营数据分析list
	 * @param bookId 账薄id 
	 * @return
	 */ 
	@GetMapping("/get/managementData/{bookId}")
	public Object findSettleAccountOperatingListData(@PathVariable("bookId") String bookId) {
		return  settleAccountOperatingSettingService.findSettleAccountOperatingListData(bookId);
	}
	
	

}
