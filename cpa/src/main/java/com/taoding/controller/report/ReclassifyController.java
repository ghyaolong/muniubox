package com.taoding.controller.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.taoding.domain.report.assetliability.ReclassifySetting;
import com.taoding.service.report.assetliability.ReclassifySettingService;

@RestController
public class ReclassifyController {
	
	@Autowired
	ReclassifySettingService reclassifySettingService;
	
	@PutMapping("/report/reclassifySetting")
	public Object updateReclassifySetting(@RequestBody ReclassifySetting reclassifySetting) {
		return reclassifySettingService.updateReclassifySetting(reclassifySetting);
	}
	
	@GetMapping("/report/reclassifySetting/{customerId}")
	public Object findReclassifySetting(@PathVariable("customerId") String customerId) {
		return reclassifySettingService.findSettingByCustomerId(customerId);
	}

}
