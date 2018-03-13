package com.taoding.controller.settleaccount;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.service.settleaccount.CpaFinalLiquidationOperatingDataService;
import com.taoding.service.settleaccount.CpaSettleAccountService;
import com.taoding.service.settleaccount.CpaSettleDealOtherAbnormalService;

/**
 * 企业期末结账
 * @author czy
 * 2017年12月26日 上午10:12:53
 */
@RestController
@RequestMapping(value = "/settleaccount")
public class CpaSettleAccountController extends BaseController{

	@Autowired
	private CpaSettleDealOtherAbnormalService settleDealOtherAbnormalService ;
	@Autowired
	private CpaSettleAccountService settleAccountService; 
	@Autowired
	private CpaFinalLiquidationOperatingDataService finalLiquidationOperatingDataService ;
	
	/**
	 * 根据账期判断结账状态
	 * 2017年12月27日 上午11:12:47
	 * @return
	 * ture 已经结账 前台操作反结账  false 未结账，前台操作结账检查
	 */
	@PostMapping("/settleState")
	public Object settleState(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		Object currentPeroid = maps.get("currentPeroid");
		
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		String peroid = "";
		if(currentPeroid != null && !"".equals(currentPeroid.toString())){
			peroid = currentPeroid.toString() ;
		}else{
			peroid = CurrentAccountingUtils.getCurrentVoucherPeriod(bookId);
		}
		return settleAccountService.dealSettleAccountState(bookId, peroid);
	}
	
	/**
	 * 获取资产类企业期末结账数据
	 * 2017年12月26日  10:11:36
	 * @param bookId
	 * @return
	 */
	@GetMapping("/listAssetData/{id}")
	public Object listAssetData(@PathVariable ("id") String bookId){
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return settleAccountService.assetClassBalance(bookId);
	}
	
	/**
	 * 获取费用类企业期末结账数据
	 * 2017年12月26日  10:11:36
	 * @param bookId
	 * @return
	 */
	@GetMapping("/listCostData/{id}")
	public Object listCostData(@PathVariable ("id") String bookId){
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return settleAccountService.costControlAndTaxAdjustment(bookId);
	}
	
	/**
	 * 获取 经营数据分析 期末结账数据
	 * 2017年12月26日  10:11:36
	 * @param bookId
	 * @return
	 */
	@GetMapping("/listOperatingData/{id}")
	public Object listOperatingData(@PathVariable ("id") String bookId){
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return finalLiquidationOperatingDataService.loadOperatingData(bookId);
	}
	
	/**
	 * 获取其他异常期末结账数据
	 * 2017年12月26日  10:11:36
	 * @param bookId
	 * @return
	 */
	@GetMapping("/listOtherData/{id}")
	public Object listOtherData(@PathVariable ("id") String bookId){
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return settleDealOtherAbnormalService.loadOtherAbnormalData(bookId);
	}
	
	/**
	 * 结账
	 * 2017年12月27日 上午11:11:25
	 * @param settleAccount
	 * @return
	 */
	@PostMapping("/settleAccount")
	public Object settleAccount(@RequestBody Map<String, String> maps){
		//生成一条结账数据
		String bookId = maps.get("bookId");
		String customerId = maps.get("customerId");
		if(StringUtils.isEmpty(bookId) || StringUtils.isEmpty(customerId)){
			throw new LogicException("参数异常");
		}
		return settleAccountService.saveSettleAccount(bookId, customerId);
	}
	
	/**
	 * 反结账
	 * 2017年12月27日 上午11:19:48
	 * @return
	 */
	@PostMapping("/antiSettleAccount")
	public Object antiSettleAccount(@RequestBody Map<String, String> maps){
		String bookId = maps.get("bookId");
		String currentPeriod = maps.get("currentPeriod");
		
		if(StringUtils.isEmpty(bookId) || StringUtils.isEmpty(currentPeriod)){
			throw new LogicException("参数异常");
		}
		// 参数 ：bookId，反结账账期 
		// 操作 ： 当前账期 及之后账期 状态改为 false 
		return settleAccountService.antiSettleAccount(bookId, currentPeriod);
	}
	
}
