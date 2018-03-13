package com.taoding.controller.settleaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.settleaccount.CpaFinalLiquidationProportion;
import com.taoding.service.settleaccount.CpaFinalLiquidationProportionService;

/**
 * 设置结转销售成本比例
 * @author czy
 * 2017年12月29日 下午2:25:08
 */
@RestController
@RequestMapping(value = "/finalproportion")
public class CpaFinalLiquidationProportionController extends BaseController{
	
	@Autowired
	private CpaFinalLiquidationProportionService proportionService;
	
	/**
	 * 保存比例
	 * @param bookId
	 * @param proportion 比例
	 * @param customerId
	 * @return
	 */
	@PutMapping("/save")
	public Object saveProportion(@RequestBody CpaFinalLiquidationProportion liquidationProportion){
		return proportionService.insertProportion(liquidationProportion);
	}

	/**
	 * 查询比例
	 * @param bookId
	 * @return
	 */
	@GetMapping("/find/{id}")
	public Object findProportion(@PathVariable("id") String bookId){
		return proportionService.findProportionByBookId(bookId);
	}
	
	/**
	 * 修改比例
	 * 2017年12月29日 下午2:40:20
	 * @param liquidationProportion
	 * @return
	 */
	@PutMapping("/update")
	public Object updateProportion(@RequestBody CpaFinalLiquidationProportion liquidationProportion){
		return proportionService.updateProportion(liquidationProportion);
	}
}
