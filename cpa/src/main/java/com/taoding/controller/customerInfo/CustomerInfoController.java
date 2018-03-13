package com.taoding.controller.customerInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.entity.DataEntity;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.PageUtils;
import com.taoding.common.utils.QueryConditionFieldsArray;
import com.taoding.common.utils.QueryConditionSql;
import com.taoding.common.utils.StringUtils;
import com.taoding.configuration.Global;
import com.taoding.domain.customerInfo.CpaCustomerLinkman;
import com.taoding.domain.customerInfo.CustomerInfo;
import com.taoding.service.customerInfo.CustomerInfoService;

/**
 * 客户管理详请Controller
 * 
 * @author mhb
 * @version 2017-11-16
 */
@RestController
@RequestMapping(value = "/customer")
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;

	/**
	 * 根据客户id查询公司信息、联系人、签约信息
	 * 
	 * @param id
	 *            公司id
	 * @return
	 */
	@GetMapping("/info/{id}")
	public Object customerInfo(@PathVariable("id") String id) {
		return customerInfoService.findCoutomerInfo(id);
	}

	/**
	 * 客户导出
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param queryCondition
	 * @param isAll
	 * @param response
	 * @return
	 * @author lixc
	 * @date 2017年11月17日11:18:58
	 */
	@PostMapping("/cutstomInfoExportData")
	public void cutstomInfoExportData(
			@RequestParam(value = "pageNo", required = false) Integer pageNo,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "queryCondition", required = false) String queryCondition,
			@RequestParam(value = "isAll", required = false) String isAll,
			HttpServletResponse response) {

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pageNo", pageNo);
		queryMap.put("pageSize", pageSize);

		if (StringUtils.isNotBlank(queryCondition)) {
			queryMap.put("queryCondition", QueryConditionSql
					.getQueryConditionSql("a",
							QueryConditionFieldsArray.customerQueryFields,
							queryCondition));
		}

		if (Global.FALSE.equals(isAll)) {
			PageUtils.page(queryMap);
		}

		queryMap.put("DEL_FLAG_NORMAL", DataEntity.DEL_FLAG_NORMAL);
		List<CustomerInfo> list = customerInfoService
				.findCustomInfoListByMap(queryMap);
		PageInfo<CustomerInfo> pages = new PageInfo<CustomerInfo>(list);
		String fileName = "客户数据" + DateUtils.getDate("yyyyMMddHHmmss")
				+ ".xlsx";
		try {
			new ExportExcel("客户数据", CustomerInfo.class)
					.setDataList(pages.getList()).write(response, fileName)
					.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 客户列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param queryCondition
	 * @return
	 * @author lixc
	 * @date 2017年11月17日11:18:58
	 */
	@PostMapping("/cutstomInfoList")
	public Object cutstomInfoList(@RequestBody  Map<String, Object> queryMap ) {

		if(null == queryMap) return  null;
		
		queryMap.put("DEL_FLAG_NORMAL", DataEntity.DEL_FLAG_NORMAL);
		String queryCondition = queryMap.get("queryCondition")==null?"":queryMap.get("queryCondition").toString();
		
		String isAll = queryMap.get("isAll")==null?"":queryMap.get("isAll").toString();
		
		if (StringUtils.isNotBlank(queryCondition)) {
			queryMap.put("queryCondition", QueryConditionSql
					.getQueryConditionSql(
							QueryConditionFieldsArray.customerQueryFields,
							queryCondition));
		}
        
		if(StringUtils.isBlank(isAll) || Global.FALSE.equals(isAll)){
			PageUtils.page(queryMap);
		}
	
		List<CustomerInfo> list = customerInfoService
				.findCustomInfoListByMap(queryMap);
		PageInfo<CustomerInfo> pages = new PageInfo<CustomerInfo>(list);
		return pages;
	}

	/**
	 * 添加公司信息
	 * 
	 * @param customerInfo
	 * @return
	 */
	@PostMapping("/saveCustomerInfo")
	public Object saveCustomerInfo(@RequestBody CustomerInfo customerInfo) {
		return customerInfoService.saveCustomerInfo(customerInfo);
	}

	/**
	 * 根据id删除公司信息
	 * 
	 * @param id
	 *            公司id
	 * @return
	 */
	@PutMapping("/deleteCustomerInfo/{id}")
	public Object deleteCustomerInfo(@PathVariable("id") String id) {
		return customerInfoService.deleteCustomerInfo(id);
	}
	
	/**
	 * 根据id启用禁用
	 * 
	 * @param id 公司id
	 * @return
	 */
	@PutMapping("/enable/{id}")
	public Object updateEnable(@PathVariable("id") String id) {
		return customerInfoService.updateEnable(id);
	}

	/**
	 * 修改客户信息
	 * 
	 * @param customerInfo
	 *            客户信息实体类
	 * @return
	 */
	@PostMapping("/editCustomerInfo")
	public Object editCustomerInfo(@RequestBody CustomerInfo customerInfo) {
		return customerInfoService.editCustomerInfo(customerInfo);
	}
	/**
	 * 添加公司联系人信息
	 * 
	 * @param customerInfo
	 * @return
	 */
	@PostMapping("/saveCustomerLinkman")
	public Object saveCustomerLinkman(
			@RequestBody CpaCustomerLinkman cpaCustomerLinkman) {
		return customerInfoService.saveCustomerLinkman(cpaCustomerLinkman);
	}

	/**
	 * 根据公司联系人id删除联系人 逻辑删除
	 * 
	 * @param id
	 *            公司id
	 * @return
	 */
	@PutMapping("/deleteCustomerLinkman/{id}")
	public Object deleteCustomerLinkman(@PathVariable("id") String id) {
		return customerInfoService.deleteCustomerLinkman(id);
	}

	/**
	 * 修改客户 联系人信息
	 * 
	 * @param cpaCustomerLinkman
	 *            客户联系人实体类
	 * @return
	 */
	@PutMapping("/editCustomerLinkman")
	public Object editCustomerLinkman(
			@RequestBody CpaCustomerLinkman cpaCustomerLinkman) {
		return customerInfoService.editCustomerLinkman(cpaCustomerLinkman);
	}

//	/**
//	 * 建账博
//	 * 
//	 * @param custormId
//	 *            客户ID
//	 * @return
//	 * @author lixc
//	 * @date 2017年11月17日15:56:16
//	 */
//	@PostMapping("/prepareAccountsBook/{custormId}")
//	public Object prepareAccountsBook(
//			@PathVariable("custormId") String custormId) {
//		// 修改客户状态为
//		CustomerInfo customerInfo = new CustomerInfo();
//		customerInfo.setId(custormId);
//		customerInfo.setAccountStatus(AccountingBook.ACCOUNT_STATUS_ALREADY);
//		customerInfoService.save(customerInfo);
//		return true;
//	}
	
	/**
	 * 获取客户编号
	 * @return
	 * @author mhb
	 */
	@GetMapping("/getNextNo")
	public Object getNextNo() {
		return customerInfoService.getNextNo();
	}

}
