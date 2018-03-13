package com.taoding.controller.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.common.controller.BaseController;
import com.taoding.domain.initialize.InitEntity;
import com.taoding.service.initialize.InitializeDataService;

/**
 * 初始化Controller
 * 
 * @author mhb
 * @version 2017-12-05
 */
@RestController
@RequestMapping(value = "/init")
public class InitializeDataController extends BaseController {
	
	
	@Autowired
	private InitializeDataService  initializeDataService;
	
	/** 
	 * 初始化
	 * @param loginName 登陆用户名称
	 * @param passWord  登陆密码
	 * @param bookType  账套类型  否(false) 是(true)
	 * @param ticketType 票据类型否(false) 是(true)
	 * @param payType  薪酬类型 否(false) 是(true)
	 * @param accountingBookId  账薄id 
	 * @return
	 */
	@PostMapping("/accountBook")
	public Object init(@RequestBody InitEntity initEntity) {
		return initializeDataService.init(initEntity);
	} 
	
	
	
	

	 

}
