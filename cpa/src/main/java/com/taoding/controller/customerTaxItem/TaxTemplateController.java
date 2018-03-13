package com.taoding.controller.customerTaxItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.customerTaxItem.TaxTemplate;
import com.taoding.service.customerTaxItem.TaxTemplateService;

/**
 * 税项模板controller
 * @author mhb
 * @version 2017-11-22
 */
@RestController
@RequestMapping(value = "/taxTemplate")
public class TaxTemplateController extends BaseController{

	@Autowired
	private TaxTemplateService taxTemplateService;

	/**
	 * 新增税项模板
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PostMapping("/save")
	public Object saveTemplate(@RequestBody TaxTemplate taxTemplate) {
		return taxTemplateService.saveTemplate(taxTemplate);
	}
	/**
	 * 修改税项模板
	 * @param customerTaxItem  实体类
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/edit")
	public Object editTemplate(@RequestBody TaxTemplate taxTemplate) {
		return taxTemplateService.editTemplate(taxTemplate);
	}
	/**
	 * 删除税项模板
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/delete/{id}")
	public Object deleteTemplate(@PathVariable("id")String id) {
		return taxTemplateService.deleteTemplate(id);
	}
	/**
	 * 启用 禁用税项模板
	 * @param id  税项模板id
	 * @return
	 * @author mhb
	 * @Date 2017年11月22日
	 */
	@PutMapping("/enable/{id}")
	public Object enableTemplate(@PathVariable("id")String id) {
		return taxTemplateService.enableTemplate(id);
	}
}
