
package com.taoding.controller.assisting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingGoods;
import com.taoding.service.assisting.CpaAssistingGoodsService;

/**
 * 辅助核算模块存货信息表Controller
 * @author csl
 * @version 2017-11-20
 */

@RestController
@RequestMapping(value = "/assisting/goods")
public class CpaAssistingGoodsController extends BaseController {

	@Autowired
	private CpaAssistingGoodsService cpaAssistingGoodsService;
	
	/**
	 * 编辑存货的条目信息
	 * @param cpaAssistingGoods
	 * @return
	 */
	@PutMapping(value="/updateGoods")
	public Object updategGoods(@RequestBody CpaAssistingGoods cpaAssistingGoods){
		return cpaAssistingGoodsService.updateGoods(cpaAssistingGoods);
	}
	
	/**
	 * 根据id删除存货
	 * @param
	 * @return
	 * add csl 
	 * 2017-11-22 14:59:56
	 */
	@DeleteMapping("/deleteGoods/{id}")
	public Object deleteGoods(@PathVariable("id") String id){
		if (StringUtils.isEmpty(id)) {
			throw new LogicException("辅助核算类型中的职位id为空！");
		}
		cpaAssistingGoodsService.deleteGoods(id);
		return true;
	}

}