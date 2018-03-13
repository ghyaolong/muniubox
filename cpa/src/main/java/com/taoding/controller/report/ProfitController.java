package com.taoding.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.domain.report.profit.ProfileCustomerFomula;
import com.taoding.service.report.profit.ProfileCustomerFomulaService;
import com.taoding.service.report.profit.ProfileCustomerHistoryService;
import com.taoding.service.report.profit.ProfitItemService;

/**
 * 利润报表
 * @author fc
 *
 */
@RestController
@RequestMapping("/profit")
public class ProfitController {

	@Autowired
	ProfileCustomerFomulaService pcfsService;
	
	@Autowired
	ProfitItemService pisService;
	
	@Autowired
	ProfileCustomerHistoryService phsService;
	
	/**
	 * 重置/初始化客户公式
	 * @author fc 
	 * @version 2017年12月27日 下午2:30:01 
	 * @param accountId
	 * @return
	 */
	@GetMapping("/resetformula/{accountId}")
	public Object resetFormula(@PathVariable("accountId") String accountId){
		return pcfsService.resetFormula(accountId);
	}
	
	/**
	 * 获取利润表
	 * @author fc 
	 * @version 2017年12月27日 下午4:57:02 
	 * @param accountId
	 * @return
	 */
	@GetMapping("/getprofititem/{accountId}")
	public Object getProfitItem(@PathVariable("accountId") String accountId){
		return pisService.getgetProfitItem(accountId);
	}
	
	/**
	 * 保存历史利润报表
	 * @author fc 
	 * @version 2017年12月29日 下午2:16:01 
	 * @return
	 */
	@PostMapping("/saveprofithistory")
	public Object saveProfitHistory(@RequestParam("accountId") String accountId){
		return phsService.saveProfitHistory(accountId);
	}
	
	/**
	 * 根据账簿id和项目id获取当前项目的公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:15:31 
	 * @param accountId
	 * @param itemId
	 * @return
	 */
	@GetMapping("/getitemfomulalist")
	public Object getItemFomulaList(@RequestParam("accountId") String accountId, @RequestParam("itemId") String itemId){
		return pcfsService.getItemFomulaList(accountId, itemId);
	}
	
	/**
	 * 保存/修改客户公式
	 * @author fc 
	 * @version 2017年12月29日 下午3:41:14 
	 * @return
	 */
	@PostMapping("/savecustomerfomula")
	public Object saveCustomerFomula(@RequestBody ProfileCustomerFomula profileCustomerFomula){
		return pcfsService.saveCustomerFomula(profileCustomerFomula);
	}
	
	/**
	 * 删除客户公式
	 * @author fc 
	 * @version 2017年12月29日 下午4:08:12 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delCustomerFomula/{id}")
	public Object delCustomerFomula(@PathVariable("id") String id){
		return pcfsService.delCustomerFomula(id);
	}
}
