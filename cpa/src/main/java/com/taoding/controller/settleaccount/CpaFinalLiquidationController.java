package com.taoding.controller.settleaccount;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.domain.settleaccount.CpaFinalLiquidation;
import com.taoding.service.settleaccount.CpaFinalLiquidationService;
import com.taoding.service.settleaccount.CpaFinalLiquidationVoucherService;
import com.taoding.service.voucher.CpaVoucherService;

/**
 * 期末结转
 * @author czy
 * 2017年12月29日 下午2:50:42
 */
@RestController
@RequestMapping(value = "/finalliquidation")
public class CpaFinalLiquidationController extends BaseController{

	@Autowired
	private CpaFinalLiquidationService finalLiquidationService;
	@Autowired
	private CpaVoucherService voucherService ;
	
	@Autowired
	private CpaFinalLiquidationVoucherService finalLiquidationVoucherService;
	
	/**
	 * 获取 企业期末结转基础数据
	 * 2017年12月21日 下午3:48:36
	 * @param bookId
	 * @return
	 */
	@PostMapping("/listData")
	public Object listData(@RequestBody Map<String,Object> maps){
		
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
		
		CpaFinalLiquidation finalLiquidation = finalLiquidationService.findByBookId(bookId, peroid);
		if(finalLiquidation != null && finalLiquidation.isSettleAccounts()){
			//调用生成过凭证数据
			return finalLiquidationVoucherService.findAllList(bookId, peroid);
		}else{
			return finalLiquidationService.loadFinalLiquidationData(bookId);
		}
	}
	
	/**
	 * 重新计算
	 * 2017年12月21日 下午3:48:36
	 * @param bookId
	 * @return
	 */
	@PostMapping("/recalculationData")
	public Object recalculationData(@RequestBody Map<String,Object> maps){
		
		String bookId = maps.get("bookId").toString();
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		return finalLiquidationService.loadFinalLiquidationData(bookId);
	}
	
	
	/**
	 * 生成凭证
	 * 2017年12月23日 上午9:55:09
	 * @return
	 */
	@PutMapping("/generateVoucher")
	public Object generateVoucher(@RequestBody CpaFinalLiquidation finalLiquidation){
		return finalLiquidationService.generateVoucher(finalLiquidation);
	}
	
	/**
	 * 查看要生成的凭证
	 * 2017年12月23日 上午9:55:09
	 * @return
	 */
	@PostMapping("/showBuildVoucher")
	public Object showBuildVoucher(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		String subKey = maps.get("subKey").toString();
		if(!StringUtils.isNotEmpty(bookId)){
			throw new LogicException("账簿ID不能为空");
		}
		if(!StringUtils.isNotEmpty(subKey)){
			throw new LogicException("查看项KEY不能为空");
		}
		return finalLiquidationService.showBuildVoucher(bookId, subKey);
	}
	
	/**
	 * 查看已经生成过的凭证
	 * 2017年12月23日 上午9:55:09
	 * @return
	 */
	@PostMapping("/showSubjectVoucher")
	public Object showSubjectVoucher(@RequestBody Map<String,Object> maps){
		String bookId = maps.get("bookId").toString();
		String voucherId = maps.get("voucherId").toString();
		return voucherService.findById(bookId, voucherId);
	}
	
	
}
