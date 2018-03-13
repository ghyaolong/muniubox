
package com.taoding.controller.assisting;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.assisting.CpaAssistingExpenseType;
import com.taoding.domain.assisting.CpaAssistingGoodsType;
import com.taoding.domain.assisting.CpaCustomerAssisting;
import com.taoding.domain.assisting.CpaCustomerAssistingInfo;
import com.taoding.service.assisting.CpaAssistingExpenseTypeService;
import com.taoding.service.assisting.CpaAssistingGoodsTypeService;
import com.taoding.service.assisting.CpaCustomerAssistingInfoService;
import com.taoding.service.assisting.CpaCustomerAssistingService;

/**
 * 辅助核算类型Controller
 * @author csl
 * @version 2017-11-16
 */
@RestController
@RequestMapping(value="/assisting")
public class CpaCustomerAssistingController extends BaseController {

	@Autowired
	private CpaCustomerAssistingService cpaCustomerAssistingService;
	
	@Autowired
	private CpaCustomerAssistingInfoService cpaCustomerAssistingInfoService;
	
	@Autowired
	private CpaAssistingExpenseTypeService cpaAssistingExpenseTypeService;
	
	@Autowired
	private CpaAssistingGoodsTypeService cpaAssistingGoodsTypeService;
	
	
	
	/**
	 * 查询辅助核算类型的列表
	 * @param 
	 * @return
	 * add csl 
	 * 2017-11-18 09:04:56
	 */
	@PostMapping(value="/listData")
	public Object findList(@RequestBody Map<String,Object> maps) {
		String accountId = maps.get("accountId").toString();
		return cpaCustomerAssistingService.findList(accountId);
	}
	
	/**
	 * 辅助核算类型中增加自定义分类
	 * @param cpaCustomerAssisting
	 * @return
	 * add csl
	 * 2017-11-18 09:05:06
	 */
	@PostMapping(value="/addCustom")
	public Object addCustom(@RequestBody CpaCustomerAssisting cpaCustomerAssisting){
		if (null==cpaCustomerAssisting) {
			return false;
		}
		cpaCustomerAssistingService.insertCustom(cpaCustomerAssisting);
		return true;
	}
	
	/**
	 * 根据id和删除自定义分类
	 * @param id
	 * @param isConstom
	 * @return
	 * add csl
	 * 2017-11-18 10:09:29
	 */
	@DeleteMapping(value="/delCustom/{id}")
	public Object delCustom(@PathVariable("id") String id){
		if (StringUtils.isBlank(id)) {
			throw new LogicException("辅助核算自定义分类的id不能为空！");
		}
		cpaCustomerAssistingService.deleteCustom(id);
		return true;
	}
	/**
	 * 根据辅助核算类型的id查找相应的列表
	 * @param request
	 * @returns
	 */
	@PostMapping(value="/findTypeListById")
	public Object findTypeListById(@RequestBody Map<String,Object> maps){
		return cpaCustomerAssistingService.findTypeListById(maps);
	}
	
	/**
	 * 根据辅助核算类型详情中的id删除
	 * 只是针对类型为客户，供应商，项目，第三方支付，自定义分类(assisting_info_type=3、4、5、8以外的类型)
	 * @param id
	 * @return
	 * add csl
	 * 2017-11-18 13:59:45
	 */
	@DeleteMapping(value="/deleteCommon/{id}")
	public Object deleteCommon(@PathVariable("id") String id){
		if (StringUtils.isNotEmpty(id)) {
			CpaCustomerAssistingInfo cpaCustomerAssistingInfo = new CpaCustomerAssistingInfo();
			cpaCustomerAssistingInfo.setId(id);
			cpaCustomerAssistingInfoService.delete(cpaCustomerAssistingInfo);
			return true;
		}
		throw new LogicException("辅助核算类型详情条目的id为空！");
	}
	
	/**
	 * 编辑辅助核算类型的详情条目
	 * 只是针对类型为客户，供应商，项目，第三方支付，自定义分类(除assisting_info_type=3、4、5、8的类型)
	 * @param cpaCustomerAssistingInfo
	 * @return
	 * add csl 
	 * 2017-11-18 14:00:01
	 */
	@PostMapping(value="/updateInfo")
	public Object updateInfo(@RequestBody CpaCustomerAssistingInfo cpaCustomerAssistingInfo){
		if (null==cpaCustomerAssistingInfo) {
			return false;
		}
		cpaCustomerAssistingInfoService.updateCpaCustomerAssistingInfo(cpaCustomerAssistingInfo);
		return true;
	}
	
	/**
	 * 新增辅助核算类型的详情条目
	 * @param 
	 * @return
	 * add csl
	 * 2017-11-18 15:18:16
	 */
	@PostMapping(value="/insertAllDetail")
	public Object insertAllDetail(@RequestBody Map<String,Object> maps){
		return cpaCustomerAssistingService.insertAllDetail(maps);
	}

	/**
	 * 查询所有的费用类型的列表
	 * @param 
	 * @return
	 * add csl 
	 * 2017-11-20 16:21:49
	 */
	@PostMapping(value="/findExpense")
	public Object findExpense(@RequestBody CpaAssistingExpenseType expenseType){
		return cpaAssistingExpenseTypeService.findList(expenseType);
	}

	/**
	 * 查询所有的存货分类的列表
	 * @param
	 * @return
	 * add csl
	 * 2017-11-21 10:23:48
	 */
	@PostMapping(value="/findGoodsType")
	public Object findGoodsType(@RequestBody CpaAssistingGoodsType capAssistingGoods){
		return cpaAssistingGoodsTypeService.findList(capAssistingGoods);
		 
	}
}