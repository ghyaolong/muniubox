package com.taoding.controller.salary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.taoding.common.controller.BaseController;
import com.taoding.common.excel.ExportExcel;
import com.taoding.common.excel.ImportExcel;
import com.taoding.common.exception.LogicException;
import com.taoding.common.utils.Collections3;
import com.taoding.common.utils.DateUtils;
import com.taoding.common.utils.StringUtils;
import com.taoding.domain.salary.CpaSalaryBankInfo;
import com.taoding.domain.salary.CpaSalaryBankTemplate;
import com.taoding.domain.salary.CpaSalaryCompanyWelfare;
import com.taoding.domain.salary.CpaSalaryEmployee;
import com.taoding.domain.salary.CpaSalaryInfo;
import com.taoding.service.salary.CpaSalaryBankInfoService;
import com.taoding.service.salary.CpaSalaryBankTemplateService;
import com.taoding.service.salary.CpaSalaryEmployeeService;
import com.taoding.service.salary.CpaSalaryInfoService;
import com.taoding.service.salary.CpaSalaryWelfareSettingService;

/**
 * 员工薪酬Controller
 * @author csl
 * @version 2017-12-13
 */
@RestController
@RequestMapping(value = "/salary/wages")
public class CpaSalaryInfoController extends BaseController {

	@Autowired
	private CpaSalaryWelfareSettingService settingService;
	
	@Autowired
	private CpaSalaryEmployeeService salaryEmployeeService;
	
	@Autowired
	private CpaSalaryInfoService cpaSalaryInfoService;
	
	@Autowired
	private CpaSalaryBankInfoService cpaSalaryBankInfoService;
	
	@Autowired
	private CpaSalaryBankTemplateService cpaSalaryBankTemplateService;
	
	/**
	 * 薪酬中新增员工（首先先在CpaSalaryEmployee员工表中增加员工，然后再向CpaSalaryInfo薪酬表添加员工信息，
	 * 再向CpaSalaryCompanyWelfare社保社保公积金列表添加员工）
	 * @param cpaSalaryEmployee
	 * @return
	 */
	@PostMapping(value="/addEmployee")
	public Object addEmployee(@RequestBody CpaSalaryEmployee cpaSalaryEmployee){
		return salaryEmployeeService.addEmployee(cpaSalaryEmployee);
	}
	
	/**
	 * 查询企业所有的员工
	 * @param cpaSalaryEmployee
	 * @return
	 */
	@PostMapping(value="/findSalaryEmployee")
	public Object findSalaryEmployee(@RequestBody CpaSalaryEmployee cpaSalaryEmployee){
		return salaryEmployeeService.findSalaryEmployeeList(cpaSalaryEmployee);
	}
	
	/**
	 * 查询薪资的列表
	 * @param maps
	 * @return
	 */
	@PostMapping(value="/findSalaryList")
	public Object findSalaryList(@RequestBody Map<String,Object> maps){
		return cpaSalaryInfoService.findAllByPage(maps);
	}
	
	/**
	 * 根据薪资id编辑员工的信息
	 * @param cpaSalaryEmployee
	 * @return
	 */
	@PutMapping("/updateEmployeeById")
	public Object updateEmployeeById(@RequestBody Map<String,Object> maps){
		return cpaSalaryInfoService.updateEmployeeById(maps);
	}
	
	/**
	 * 根据薪资id删除员工
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteSalaryById/{id}")
	public Object deleteSalaryById(@PathVariable("id") String id){
		return cpaSalaryInfoService.deleteEmployeeById(id);
	}
	
	/**
	 * 重新计算薪酬(需要传递企业customerId)
	 * @param customerId
	 * @return
	 */
	@PostMapping("/refreshSalaryInfo/{customerId}")
	public Object refreshSalaryInfo(@PathVariable("customerId") String customerId){
		if (StringUtils.isEmpty(customerId)) {
			throw new LogicException("企业不存在！");
		}
		//重新计算企业所有的社保公积金信息列表
		settingService.refreshWelfare(customerId);
		//重新计算薪资的信息列表
		cpaSalaryInfoService.refreshSalaryInfo(customerId);
		return true;
	}
	
	/**
	 * 快速调整薪酬列表(需要传递企业所有的薪酬列表信息)
	 * @param cpaSalaryInfo
	 * @return
	 */
	@PostMapping("/updateSalaryList")
	public Object updateSalaryList(@RequestBody CpaSalaryInfo cpaSalaryInfo){
		return cpaSalaryInfoService.updateSalaryInfo(cpaSalaryInfo);
	}
	
	/**
	 * 薪资批量设置工资(传递薪资对象的customerId和多个lists包含有基本工资，补助和customerId)
	 * @param cpaSalaryInfo
	 * @return
	 */
	@PutMapping("/batchUpdateSalary")
	public Object batchUpdateSalary(@RequestBody CpaSalaryInfo cpaSalaryInfo){
		return cpaSalaryInfoService.batchUpdateSalary(cpaSalaryInfo);
	}
	
	/**
	 * 薪资批量设置基数(包含社保公积金的对象的customerId和多个list所有基数CpaSalaryCompanyWelfare的 id )
	 * @param cpaSalaryCompanyWelfare
	 * @return
	 */
	@PutMapping("/batchUpdateBasic")
	public Object batchUpdateBasic(@RequestBody CpaSalaryCompanyWelfare cpaSalaryCompanyWelfare){
		return cpaSalaryInfoService.batchUpdateBasic(cpaSalaryCompanyWelfare);
	}
	
	/**
	 * 调整本期个税(传递薪资对象的customerId和lists包含有免税所得，其他税前扣除，准予扣除捐赠，允许扣除税费，减免税费)
	 * 注：只能传入单条数据，不允许传入多条数据
	 * @param cpaSalaryInfo
	 * @return
	 */
	@PutMapping("/updateAdjustTax")
	public Object updateAdjustTax(@RequestBody CpaSalaryInfo cpaSalaryInfo){
		return cpaSalaryInfoService.updateAdjustTax(cpaSalaryInfo);
	}
	
	/**
	 * 批量删除员工和薪酬（传入薪酬的id）
	 * @param cpaSalaryInfo
	 * @return
	 */
	@DeleteMapping("/batchDeleteSalary")
	public Object batchDeleteSalary(@RequestBody CpaSalaryInfo cpaSalaryInfo){
		return cpaSalaryInfoService.batchDeleteSalary(cpaSalaryInfo);
	}
	
	
	/**
	 * 导入员工数据
	 * @param file
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/importEmployeeData")
	public Object importEmployeeData(@Param("file") MultipartFile file,@Param("customerId") String customerId,@Param("response") HttpServletResponse response) throws Exception {
		ImportExcel ei = new ImportExcel(file, 0, 0);
		List<CpaSalaryEmployee> list = ei.getDataListTwo(CpaSalaryEmployee.class);
		if (Collections3.isEmpty(list)) {
			return false;
		}
		return salaryEmployeeService.importEmployeeData(list,customerId);
	}
	
	
	/**
	 * 调整个税起征点(传递客户id，账期，薪酬id和个税起征点数据)
	 * @param cpaSalaryInfo
	 * @return
	 */
	@PostMapping("/updateIndividualTaxLevy")
	public Object updateIndividualTaxLevy(@RequestBody CpaSalaryInfo cpaSalaryInfo){
		return cpaSalaryInfoService.updateIndividualTaxLevy(cpaSalaryInfo);
	}
	
	/**
	 * 导出薪酬列表
	 * @param maps
	 * @param response
	 * @return
	 */
	@PostMapping("/exportSalaryInfoData")
	public Object exportSalaryInfoData(@RequestBody Map<String, Object> maps,HttpServletResponse response){
		PageInfo<CpaSalaryInfo> pages = cpaSalaryInfoService.findAllByPage(maps);
		String fileName = "薪酬"+DateUtils.getDate("yyyyMMdd")+".xlsx";
		try {
			new ExportExcel("薪酬", CpaSalaryInfo.class).setDataList(pages.getList()).write(response,fileName).dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 获取计提工资
	 * @return
	 */
//	@PostMapping("/getAccruedSalary/{customerId}")
//	public Object getAccruedSalary(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getAccruedSalary(customerId);
//	}
	
	/**
	 * 获取代扣五险一金
	 * @param cpaSalaryInfo
	 * @return
	 */
//	@PostMapping("/getWithHoldAccount/{customerId}")
//	public Object getWithHoldAccount(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getWithHoldAccount(customerId);
//	}
	
	/**
	 * 获取代扣个税
	 * @param cpaSalaryInfo
	 * @return
	 */
//	@PostMapping("/getIndividualTax/{customerId}")
//	public Object getIndividualTax(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getIndividualTax(customerId);
//	}
	
	/**
	 * 现金发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
//	@PostMapping("/getCashSalary/{customerId}")
//	public Object getCashSalary(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getCashSalary(customerId);
//	}
//
	/**
	 * 银行发放工资
	 * @param cpaSalaryInfo
	 * @return
	 */
//	@PostMapping("/getBankSalary/{customerId}")
//	public Object getBankSalary(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getBankSalary(customerId);
//	}
	
	/**
	 * 获取企业的个人缴纳的社保公积金的分项信息
	 * @param customerId
	 * @return
	 */
//	@PostMapping("/getIndividualSocialSecurity/{customerId}")
//	public Object getIndividualSocialSecurity(@PathVariable("customerId") String customerId){
//		return cpaSalaryInfoService.getIndividualSocialSecurity(customerId);
//	}

	/**
	 * 个税反算工资
	 * @param cpaIndividualTaxInverseSalary
	 * @return
	 */
//	@PostMapping("/inverseSalary")
//	public Object inverseSalary(@RequestBody CpaIndividualTaxInverseSalary cpaIndividualTaxInverseSalary){
//		
//	}

	/**
	 * 薪酬设置中薪资发放方式
	 * @author fc 
	 * @version 2017年12月21日 下午3:04:03 
	 * @return
	 */
	@GetMapping("/getsalaryprovideway")
	public Object getSalaryProvideWay(){
		List<Map<String,Object>> wayList = new ArrayList<Map<String,Object>>();
		Map<String,Object> way1 = new HashMap<String, Object>();
		Map<String,Object> way2 = new HashMap<String, Object>();
		way1.put("value", -1);
		way1.put("name", "现金发放工资");
		way2.put("value", 0);
		way2.put("name", "银行代发工资");
		wayList.add(way1);
		wayList.add(way2);
		return wayList;
	}
	
	/**
	 * 薪酬设置中银行模板列表
	 * @author fc 
	 * @version 2017年12月21日 下午4:21:40 
	 * @return
	 */
	@GetMapping("/salarybanktemplatelist")
	public Object salaryBankTemplateList(){
		List<CpaSalaryBankTemplate> sbtList = cpaSalaryBankTemplateService.salaryBankTemplateList();
		return sbtList;
	}
	
	/**
	 * 根据银行模板id获取银行模板详情
	 * @author fc 
	 * @version 2017年12月21日 下午4:43:08 
	 * @return
	 */
	@GetMapping("banktemplateById/{id}")
	public Object getBankTemplateById(@PathVariable("id") String id){
		CpaSalaryBankTemplate sbt = cpaSalaryBankTemplateService.getBankTemplateById(id);
		return sbt;
	}
	
	/**
	 * 根据银行模板code获取银行模板详情
	 * @author fc 
	 * @version 2017年12月21日 下午4:43:08 
	 * @return
	 */
	@GetMapping("getbanktemplateByCode/{code}")
	public Object getBankTemplateByCode(@PathVariable("code") String code){
		CpaSalaryBankTemplate sbt = cpaSalaryBankTemplateService.getBankTemplateByCode(code);
		return sbt;
	}
	
	/**
	 * 保存/修改薪酬设置
	 * @author fc 
	 * @version 2017年12月21日 下午5:25:52 
	 * @param cpaSalaryBankInfo
	 * @return
	 */
	@PostMapping("/savesalarysetting")
	public Object saveSalarySetting(@RequestBody CpaSalaryBankInfo cpaSalaryBankInfo){
		return cpaSalaryBankInfoService.saveSalarySetting(cpaSalaryBankInfo);
	}
	
	/**
	 * 获取当前客户薪酬设置
	 * @author fc 
	 * @version 2017年12月22日 上午9:27:19 
	 * @return
	 */
	@GetMapping("/getsalarysetting/{customerId}")
	public Object getSalarysetting(@PathVariable("customerId") String customerId){
		return cpaSalaryBankInfoService.getSalarysetting(customerId);
	}
}