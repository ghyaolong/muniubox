
package com.taoding.controller.salary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.service.salary.CpaSalaryWelfareTemplateService;

/**
 * 社保公积金模板Controller
 * @author csl
 * @version 2017-11-24
 */
@RestController
@RequestMapping(value = "/salary")
public class CpaSalaryWelfareTemplateController extends BaseController {

	@Autowired
	private CpaSalaryWelfareTemplateService cpaSalaryWelfareTemplateService;
	
}