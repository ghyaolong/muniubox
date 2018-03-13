package com.taoding.controller.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.service.ticket.ListTemplateService;

@RestController
@RequestMapping("/list/template")
public class ListTemplateController extends BaseController {
	
	@Autowired
	private ListTemplateService listTemplateService;
	
	@PostMapping("/new")
	public void insertTemplate(String tableName, String template) {
		listTemplateService.insertTemplate(tableName, template);
	}
	
}
