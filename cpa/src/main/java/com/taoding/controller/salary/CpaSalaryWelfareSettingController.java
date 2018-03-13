
package com.taoding.controller.salary;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.StringUtil;
import com.taoding.common.controller.BaseController;
import com.taoding.common.exception.LogicException;
import com.taoding.domain.salary.CpaSalaryWelfareSetting;
import com.taoding.service.salary.CpaSalaryRoundRuleService;
import com.taoding.service.salary.CpaSalaryWelfareProjectService;
import com.taoding.service.salary.CpaSalaryWelfareSettingService;
import com.taoding.service.salary.CpaSalaryWelfareTemplateService;
import com.taoding.service.salary.CpaSalaryWelfareTypeService;

/**
 * 客户社保公积金配置Controller
 * @author csl
 * @version 2017-11-24
 */
@RestController
@RequestMapping(value = "/salary")
public class CpaSalaryWelfareSettingController extends BaseController {

	@Autowired
	private CpaSalaryWelfareSettingService cpaSalaryWelfareSettingService;
	
	@Autowired
	private CpaSalaryRoundRuleService cpaSalaryRoundRuleService;
	
	@Autowired
	private CpaSalaryWelfareTemplateService cpaSalaryWelfareTemplateService;
	
	@Autowired
	private CpaSalaryWelfareTypeService cpaSalaryWelfareTypeService;
	
	@Autowired
	private CpaSalaryWelfareProjectService cpaSalaryWelfareProjectService;
	
	/**
	 * 获取企业所有的社保/公积金方案
	 * @param
	 * @return
	 */
	@PostMapping("/listData")
	public Object findNameByAccountId(@RequestBody Map<String,Object> maps){
		return cpaSalaryWelfareSettingService.findProjectAndItemByCutomerId(maps.get("customerId").toString());
	}

	/**
	 * 根据id删除企业的社保公积金缴纳的项目
	 * @param id
	 * @return
	 */
	@DeleteMapping("/deleteById/{id}")
	public Object deleteById(@PathVariable("id") String id){
		if (StringUtil.isEmpty(id)) {
			throw new LogicException("社保/公积金缴纳的项目id不能为空！");
		}
		cpaSalaryWelfareSettingService.deleteById(id);
		return true;
	}
	
	/**
	 * 根据方案名称删除方案(传递方案名称id)
	 * @param maps
	 * @return
	 */
	@PostMapping("/deleteByName")
	public Object deleteByName(@RequestBody Map<String,Object> maps){
		String projectId=maps.get("projectId").toString();
		String customerId=maps.get("customerId").toString();
		if (StringUtil.isEmpty(projectId)|| StringUtil.isEmpty(customerId)) {
			throw new LogicException("方案名称或者客户id不存在！");
		}
		return cpaSalaryWelfareSettingService.deleteByName(projectId,customerId);
	}
	
	/**
	 * 查询所有的取整规则
	 * @param
	 * @return
	 */
	@GetMapping("/findRuleList")
	public Object findRuleList(){
		return cpaSalaryRoundRuleService.findRuleList();
	}
	
	/**
	 * 根据城市名称查找所有的社保公积金模板方案
	 * @param maps
	 * @return
	 */
	@PostMapping("/findTemplatebyCityName")
	public Object findTemplatebyCityName(@RequestBody Map<String,Object> maps){
		String cityName=maps.get("cityName").toString();
		if (StringUtil.isEmpty(cityName)) {
			throw new LogicException("城市名称不存在!");
		}
		return cpaSalaryWelfareTemplateService.findListByCityName(cityName);
	}
	
	/**
	 * 查询所有的城市名称
	 * @param
	 * @return
	 */
	@GetMapping("/findAllCityName")
	public Object findAllCityName(){
		return cpaSalaryWelfareTemplateService.findAllCityName();
	}
	
	/**
	 * 查询所有的社保公积金缴纳项目的名称
	 * @param
	 * @return
	 */
	@GetMapping("/findAllTypeName")
	public Object findAllTypeName(){
		return cpaSalaryWelfareTypeService.findAllNameList();
	}
	
	/**
	 * 查询企业所有的社保公积金方案的名称id
	 * @param  maps
	 * @return
	 * 
	 */
	@PostMapping("/findAllProjectList")
	public Object findAllProjectList(@RequestBody Map<String,Object> maps){
		return cpaSalaryWelfareProjectService.findAllList(maps.get("customerId").toString());
	}
	/**
	 * 修改或者新增企业社保公积金方案
	 * @param maps
	 * @return
	 */
	@PostMapping("/saveSalaryProject")
	public Object saveSalaryProject(@RequestBody CpaSalaryWelfareSetting cpaSalaryWelfareSetting){
		return cpaSalaryWelfareSettingService.saveProject(cpaSalaryWelfareSetting);
	}
}