
package com.taoding.controller.salary;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.CurrentAccountingUtils;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.service.salary.CpaSalaryCompanyWelfareService;

/**
 * 客户员工社保公积金表Controller
 * @author csl
 * @version 2017-11-24
 */
@RestController
@RequestMapping(value = "/salary/fund")
public class CpaSalaryCompanyWelfareController extends BaseController {

	@Autowired
	private CpaSalaryCompanyWelfareService cpaSalaryCompanyWelfareService;
	
	
	/**
	 * 根据账期查询企业员工的社保公积金(传递参数customerId)
	 * @param maps
	 * @return
	 */
	@PostMapping("/listWelfare")
	public Object findListByAccountIdAndPeriod(@RequestBody Map<String,Object> maps){
		if (StringUtils.isEmpty(maps.get("customerId").toString())) {
			throw new LogicException("企业信息不明确！");
		}
		String customerId = maps.get("customerId").toString();
		String period = (String) maps.get("period");
		//获取当前账期
		String currentPeriod = CurrentAccountingUtils.getCurrentVoucherPeriod(customerId);
		if (currentPeriod==null) {
			throw new LogicException("无法获取当前账期！");
		}
		//如果当前账期为空，则根据当前账期查询
		if (StringUtils.isEmpty(period)) {
			//获取当前账期
			period = currentPeriod;
			maps.put("period", period);
		}
		return cpaSalaryCompanyWelfareService.findWelfare(maps);
	}
	
	/**
	 * 下载当前客户社保公积金列表(不符合原型要求)
	 * @param maps
	 * @param response
	 */
	@RequestMapping("/downloadData")
	public void downloadData(@RequestBody Map<String, Object> maps,HttpServletResponse response){
		PageInfo<CpaSalaryCompanyWelfare> pages = cpaSalaryCompanyWelfareService.findWelfare(maps);
		String fileName = "社保公积金统计表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try {
			new ExportExcel("社保公积金统计表", CpaSalaryCompanyWelfare.class).setDataList(pages.getList()).write(response, fileName)
					.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 快速调整客户社保公积金详情
	 * @param maps
	 * @return
	 * @throws ParseException 
	 */
	@PostMapping("/saveWelfare")
	public Object saveWelfare(@RequestBody CpaSalaryCompanyWelfare companyWelfare) throws ParseException{
		String customerId = companyWelfare.getCustomerId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//传递的参数period
		
		Date currentPeriod =sdf.parse(CurrentAccountingUtils.getCurrentVoucherPeriod(customerId).toString()) ;
		//如果当前账期为空，则根据当前账期查询
			if (null==companyWelfare.getList().get(0).getPeriod()) {
				companyWelfare.setPeriod(currentPeriod);
			}	
			//如果当前账期不为空时
			//判断当前账期是否与传递的账期相等
			if (companyWelfare.getList().get(0).getPeriod().before(currentPeriod)) {
				throw new LogicException("该账期已结账！不可修改");
			}
		cpaSalaryCompanyWelfareService.saveWelfare(companyWelfare);
		return true;
	}
	
	/**
	 * 获取企业公司缴纳的五险一金的总和
	 * @param customerId
	 * @return
	 */
//	@PostMapping("/getCompanyTax/{customerId}")
//	public Object getCompanyTax(@PathVariable("customerId") String customerId){
//		return cpaSalaryCompanyWelfareService.getCompanyTax(customerId);
//	}
	
	/**
	 * 获取企业缴纳员工的五险一金的各分项和
	 * @param customerId
	 * @return
	 */
//	@PostMapping("/getCompanySocialSecurity/{customerId}")
//	public Object getCompanySocialSecurity(@PathVariable("customerId") String customerId){
//		return cpaSalaryCompanyWelfareService.getCompanySocialSecurity(customerId);
//	}
}